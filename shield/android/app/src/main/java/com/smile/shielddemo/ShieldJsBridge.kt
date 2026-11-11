package com.smile.shielddemo

import android.util.Log

/**
 * Receives the Shield SDK callbacks from the WebView.
 *
 * The page calls `ShieldNative.onShieldEvent(type, jsonPayload)` for every
 * `onProfilingStarted` / `onProfilingCompleted` / `onError`; we log the raw
 * payload under the `ShieldDemo` tag so it can be inspected with
 * `adb logcat -s ShieldDemo`.
 */
class ShieldJsBridge(private val onRequestToken: () -> Unit = {}) {

    /**
     * The page asks for a `userToken` when the Start button is pressed. The request happens natively
     * because the page runs on an https origin and an http fetch would be mixed content.
     */
    @android.webkit.JavascriptInterface
    fun requestToken() {
        Log.i(TAG, "page requested a userToken")
        onRequestToken()
    }

    @android.webkit.JavascriptInterface
    fun onShieldEvent(type: String, payload: String) {
        Log.i(TAG, "callback=$type payload=$payload")
    }

    @android.webkit.JavascriptInterface
    fun onShieldLog(message: String) {
        Log.d(TAG, message)
    }

    companion object {
        const val TAG = "ShieldDemo"
    }
}
