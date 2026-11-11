package com.smile.shielddemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebViewAssetLoader

/**
 * Minimal Android host for the Shield H5 SDK.
 *
 * The Shield SDK is a JavaScript bundle, so a native app hosts it in a WebView.
 * This demo keeps everything in `assets/shield.html` and only bridges the
 * callbacks back to Kotlin through [ShieldJsBridge].
 */
class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            mediaPlaybackRequiresUserGesture = false
        }
        // Serve assets from https://appassets.androidplatform.net so the page has a real
        // https origin. A `file://` page sends `Origin: null` and the Link API call is
        // rejected by CORS.
        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .build()
        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest,
            ): WebResourceResponse? = assetLoader.shouldInterceptRequest(request.url)
        }
        // The Start button in the page drives everything: it asks for a token through this bridge,
        // so nothing runs until the user presses it and every press re-initialises the SDK.
        webView.addJavascriptInterface(ShieldJsBridge { requestToken() }, "ShieldNative")
        webView.loadUrl("https://appassets.androidplatform.net/assets/shield.html")
    }

    /** Fetches a token from the demo server and hands it to the waiting page. */
    private fun requestToken() {
        TokenFetcher.fetch(
            onSuccess = { token ->
                webView.evaluateJavascript("window.onNativeToken(${quote(token)})", null)
            },
            onError = { message ->
                Log.e(ShieldJsBridge.TAG, "cannot get userToken: $message")
                webView.evaluateJavascript("window.shieldTokenFailed(${quote(message)})", null)
            },
        )
    }

    private fun quote(value: String): String =
        "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\""

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}
