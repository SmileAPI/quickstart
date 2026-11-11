/**
 * Shield SDK wrapper for the native demos.
 *
 * Nothing starts on page load: the Start button is the only entry point, so a demo can be run,
 * re-run and compared. Every click disposes the previous SmileShield instance and builds a new one,
 * which re-initialises the SDK against a fresh session.
 *
 * Callbacks are forwarded to the host so the platform can print them:
 *   Android → `ShieldNative.onShieldEvent(type, json)`  → logcat tag `ShieldDemo`
 *   iOS     → `window.webkit.messageHandlers.shield.postMessage({type, payload})`
 */
(function () {
    'use strict';

    var logEl = document.getElementById('log');
    var statusEl = document.getElementById('status');
    var startBtn = document.getElementById('startBtn');

    /** Endpoint of the quickstart Node server that mints the userToken (iOS path). */
    var TOKEN_ENDPOINT = 'http://127.0.0.1:8000/api/create_link_token';

    /** The live instance, so the next click can dispose it. */
    var shield = null;

    function log(message, kind) {
        var t = new Date().toTimeString().slice(0, 8);
        var span = document.createElement('span');
        span.className = kind || '';
        span.textContent = '[' + t + '] ' + message + '\n';
        logEl.appendChild(span);
        logEl.scrollTop = logEl.scrollHeight;
    }

    function status(text) {
        statusEl.textContent = text || '';
    }

    function setBusy(busy) {
        startBtn.disabled = busy;
        startBtn.textContent = busy ? 'Running…' : (shield ? 'Run again' : 'Start Shield');
    }

    function setStatus(info) {
        if (info.sessionId) document.getElementById('stSession').textContent = info.sessionId;
        if (info.userId) document.getElementById('stUser').textContent = info.userId;
        if (info.profilingResultCode) document.getElementById('stSignal').textContent = info.profilingResultCode;
        if (info.durationMs) document.getElementById('stVia').textContent = info.durationMs + 'ms';
    }

    /** Forward a callback to the native host and to the page log. */
    function emit(type, payload) {
        var json = JSON.stringify(payload);
        log(type + ' ' + json, 'ok');
        try {
            if (window.ReactNativeWebView && window.ReactNativeWebView.postMessage) {
                // React Native
                window.ReactNativeWebView.postMessage(JSON.stringify({ type: type, payload: json }));
            } else if (window.ShieldBridge && window.ShieldBridge.postMessage) {
                // Flutter (JavaScriptChannel)
                window.ShieldBridge.postMessage(JSON.stringify({ type: type, payload: json }));
            } else if (window.ShieldNative && window.ShieldNative.onShieldEvent) {
                // Android native demo
                window.ShieldNative.onShieldEvent(type, json);
            } else if (window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.shield) {
                // iOS native demo
                window.webkit.messageHandlers.shield.postMessage({ type: type, payload: json });
            }
        } catch (e) {
            log('bridge error: ' + e, 'err');
        }
    }

    /** Drop the previous instance: stop profiling and remove the loading overlay. */
    function disposePrevious() {
        if (!shield) return;
        try {
            shield.close();             // stop + reset, so the instance could even be reused
        } catch (e) {
            log('close() failed: ' + e, 'err');
            shield.stop();
        }
        shield = null;
        // Defensive: never leave a stale overlay behind between runs
        var roots = document.querySelectorAll('[data-smile-shield-root]');
        for (var i = 0; i < roots.length; i++) {
            roots[i].remove();
        }
    }

    /** Asks the platform / Node server for a userToken, then starts the SDK. */
    function requestToken(then) {
        if (window.ReactNativeWebView && window.ReactNativeWebView.postMessage) {
            // React Native: let the native side fetch the token
            status('requesting userToken from the demo server…');
            window.ReactNativeWebView.postMessage(JSON.stringify({ type: 'requestToken' }));
            return;
        }
        if (window.ShieldBridge && window.ShieldBridge.postMessage) {
            // Flutter: let the native side fetch the token
            status('requesting userToken from the demo server…');
            window.ShieldBridge.postMessage('requestToken');
            return;
        }
        if (window.ShieldNative && window.ShieldNative.requestToken) {
            // Android: the page runs on https, so an http fetch would be mixed content
            status('requesting userToken from the demo server…');
            window.ShieldNative.requestToken();
            window.__pendingStart = then;
            return;
        }
        status('requesting userToken…');
        fetch(TOKEN_ENDPOINT)
            .then(function (response) { return response.json(); })
            .then(function (data) {
                var token = data && (data.accessToken || (data.token && data.token.accessToken));
                if (!token) throw new Error('accessToken missing in response');
                then(token);
            })
            .catch(function (error) {
                window.shieldTokenFailed(error.message);
            });
    }

    /** Called by the native host when it could not obtain a userToken. */
    window.shieldTokenFailed = function (message) {
        status('');
        setBusy(false);
        log('cannot get userToken — ' + message + '. Is the Node server on :8000 running?', 'err');
    };

    /** Entry point used by both the Start button and the native host. */
    window.startShield = function (userToken) {
        if (!window.SmileShield) {
            log('SmileShield global is missing — check the SDK <script> tag', 'err');
            return;
        }
        disposePrevious();

        shield = new SmileShield({
            userToken: userToken,

            // The loading page is always shown; this demos layout + copy + theme
            ui: {
                layout: 'popup',
                title: 'Verifying your device',
                description: "We're running a quick security check to help protect your account.",
                hint: 'This usually takes a few seconds.\nPlease stay on this screen.',
                primaryColor: '#0B9758'
            },

            onProfilingStarted: function (info) {
                emit('onProfilingStarted', {
                    sessionId: info.sessionId,
                    userId: info.userId || null,
                    startedAt: info.startedAt
                });
                setStatus(info);
                status('profiling…');
            },

            onProfilingFinished: function (info) {
                emit('onProfilingFinished', {
                    sessionId: info.sessionId,
                    userId: info.userId || null,
                    profilingResultCode: info.profilingResultCode,
                    durationMs: info.durationMs,
                    finishedAt: info.finishedAt
                });
                setStatus(info);
                status('done — next step is the server-side POST /shield/sessions/' + info.sessionId + '/assessments');
            },

            onError: function (error) {
                emit('onError', { code: error.code, message: error.message });
                status('error: ' + error.code);
            }
        });

        shield.start().then(function (info) {
            log('start() resolved resultCode=' + info.profilingResultCode + ' duration=' + info.durationMs + 'ms', 'ok');
            setBusy(false);
        }).catch(function (error) {
            log('start() rejected: ' + error.message, 'err');
            setBusy(false);
        });
    };

    /** Android bridge callback: the native side finished fetching the token. */
    window.onNativeToken = function (userToken) {
        window.startShield(userToken);
    };

    startBtn.addEventListener('click', function () {
        if (startBtn.disabled) return;
        setBusy(true);
        log('──── start ────');
        requestToken(function (token) {
            log('userToken received', 'ok');
            window.startShield(token);
        });
    });

    log('page ready — press Start to run the Shield SDK');
    setBusy(false);
})();
