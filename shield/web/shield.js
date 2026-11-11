/**
 * Smile Shield quickstart (served by ../node as `/shield.html`).
 *
 * The token comes from the existing demo server: `GET /api/create_link_token`
 * creates a user through the Smile Open API and returns `data.accessToken`,
 * exactly like the Wink widget sample (`index.html`).
 */
(function () {
    'use strict';

    var logEl = document.getElementById('log');
    var startBtn = document.getElementById('start');
    var statusEl = document.getElementById('status');

    function log(message, kind) {
        var time = new Date().toTimeString().slice(0, 8);
        var ts = document.createElement('span');
        ts.className = 'ts';
        ts.textContent = '[' + time + '] ';
        var span = document.createElement('span');
        span.className = kind || '';
        span.textContent = message + '\n';
        logEl.appendChild(ts);
        logEl.appendChild(span);
        logEl.scrollTop = logEl.scrollHeight;
    }

    function setStatus(info) {
        statusEl.hidden = false;
        document.getElementById('stSession').textContent = info.sessionId || '—';
        document.getElementById('stUser').textContent = info.userId || '—';
        document.getElementById('stSignal').textContent = info.profilingResultCode || '—';
        document.getElementById('stVia').textContent = info.profilingResultCode
            ? info.profilingResultCode + ' / ' + info.durationMs + 'ms'
            : '—';
    }

    startBtn.addEventListener('click', function () {
        startBtn.disabled = true;
        logEl.textContent = '';

        if (!window.SmileShield) {
            log('SmileShield global is missing — check the SDK <script> tag.', 'err');
            startBtn.disabled = false;
            return;
        }

        log('GET /api/create_link_token …');
        fetch('/api/create_link_token')
            .then(function (response) {
                if (!response.ok) throw new Error('create_link_token failed: HTTP ' + response.status);
                return response.json();
            })
            .then(function (data) {
                log('userToken received', 'ok');

                // Two display switches:
                //   enabled — show the SDK loading page at all (default true)
                //   layout  — 'popup' (modal) or 'page' (full-screen page)
                var layout = document.getElementById('loadingLayout').value;
                var title = document.getElementById('uiTitle').value.trim() || 'Getting things ready';
                var brandColor = document.getElementById('uiColor').value;
                log('ui = { layout: \'' + layout + '\', title: \'' + title +
                    '\', brandColor: \'' + brandColor + '\' }');

                var shield = new SmileShield({
                    userToken: data.accessToken,

                    // The loading page is always shown; customize layout, copy and brand color here
                    ui: {
                        layout: layout,
                        title: title,
                        brandColor: brandColor
                    },

                    onProfilingStarted: function (info) {
                        log('onProfilingStarted  sessionId=' + info.sessionId +
                            ' userId=' + (info.userId || '-'), 'ok');
                        setStatus({ sessionId: info.sessionId, userId: info.userId });
                    },

                    onProfilingFinished: function (info) {
                        log('onProfilingFinished  sessionId=' + info.sessionId +
                            ' userId=' + (info.userId || '-') +
                            ' profilingResultCode=' + info.profilingResultCode, 'ok');
                        setStatus(info);
                    },

                    onError: function (error) {
                        log('onError ' + error.code + ': ' + error.message, 'err');
                    }
                });

                return shield.start().then(function (info) {
                    log('start() resolved: resultCode=' + info.profilingResultCode + ' duration=' + info.durationMs + 'ms', 'ok');
                    log('Next step (server side): POST /shield/sessions/' + info.sessionId + '/assessments', 'ok');
                    setStatus(info);
                });
            })
            .catch(function (error) {
                log(String(error && error.message ? error.message : error), 'err');
            })
            .then(function () {
                startBtn.disabled = false;
            });
    });
})();
