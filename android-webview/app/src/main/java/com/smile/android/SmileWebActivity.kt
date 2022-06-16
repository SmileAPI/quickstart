package com.smile.android

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smile.android.entity.ResultModel
import com.smile.android.entity.SmileJsModel
import com.smile.android.util.HttpCallBack
import com.smile.android.util.OkhttpUtils


class SmileWebActivity : AppCompatActivity(), SmileBridgeInterface {
    private var filePathCallback: ValueCallback<Array<Uri>>? = null


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        Log.d("TestActivityTag", "init")
        val wv = findViewById<WebView>(R.id.webview_smile_view)
        wv.settings.javaScriptEnabled = true
        wv.settings.allowContentAccess = true
        wv.settings.javaScriptCanOpenWindowsAutomatically = true
        wv.settings.allowFileAccess = true
        wv.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        wv.settings.allowFileAccessFromFileURLs = true
        wv.addJavascriptInterface(SmileJsCallBack(this), "smile")

        with(OkhttpUtils) {
            //Call the node server to get user token.
            getInstance().getRequest(
                "http://10.0.2.2:8000/api/create_link_token",
                object : HttpCallBack {
                    override fun onError(errorLog: String) {
                        Log.e("errorLog", errorLog)
                    }
                    override fun onSuccess(response: String) {
                        Log.d("response", response)
                        try {
                            val type = object : TypeToken<ResultModel>() {}.type
                            val model: ResultModel = Gson().fromJson(response, type)
                            val enableUpload = true
                            val providers: List<String> = emptyList()
                            val topProviders: List<String> = emptyList()
                            val providerTypes: List<String> = emptyList()
                            val jsModel = SmileJsModel(
                                /**
                                 * The Link API URI. Note: Sandbox and Production modes are using different API URIs.
                                 */
                                apiHost = model.apiHost,
                                /**
                                 * Use provider id to filter provider list. Example ['upwork', 'freelancer']
                                 */
                                providers = providers,
                                /**
                                 * User token passed from your backend service which is obtained from the Smile API.
                                 */
                                userToken = model.accessToken,

                                enableUpload = enableUpload,
                                /**
                                 * Use provider id to filter provider list. Example ['upwork', 'freelancer']
                                 */
                                topProviders = topProviders,
                                /**
                                 * Use provider type to filter provider list
                                 */
                                providerTypes = providerTypes)
                            runOnUiThread {wv.loadUrl("file:///android_asset/smile.html?data=${Gson().toJson(jsModel)}")}
                        } catch (e: Exception) {
                            Log.e("try exception", e.message.toString())
                        }
                    }
                })
        }
        wv.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.e("TestActivityTag", "onPageFinished")
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.e("TestActivityTag", "onPageStarted")
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                Log.e("TestActivityTag", "onLoadResource")
            }
        }
        wv.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                this@SmileWebActivity.filePathCallback = filePathCallback
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "image/*"
                startActivityForResult(i, 1)
                return true
            }
        }

    }

    override fun onAccountCreated(accountId: String, userId: String, providerId: String) {
        Log.d("accountId", accountId)
        Log.d("userId", userId)
        Log.d("providerId", providerId)
    }


    override fun onAccountConnected(accountId: String, userId: String, providerId: String) {
        Log.d("accountId", accountId)
        Log.d("userId", userId)
        Log.d("providerId", providerId)
    }

    override fun onAccountRemoved(accountId: String, userId: String, providerId: String) {
        Log.d("accountId", accountId)
        Log.d("userId", userId)
        Log.d("providerId", providerId)
    }

    override fun onClose() {
        Log.e("onClose", "")
    }

    override fun onUploadsCreated(uploads: String?, userId: String) {
        Log.d("uploads", uploads.toString())
        Log.d("userId", userId)
    }

    override fun onTokenExpired(updateToken: String) {
        Log.d("updateToken", updateToken)
    }

}


class SmileJsCallBack(val listener: SmileBridgeInterface) : Any() {

    @JavascriptInterface
    fun onAccountConnected(accountId: String, userId: String, providerId: String) {
        listener.onAccountConnected(accountId, userId, providerId)
    }

    @JavascriptInterface
    fun onAccountRemoved(accountId: String, userId: String, providerId: String) {
        listener.onAccountRemoved(accountId, userId, providerId)
    }

    @JavascriptInterface
    fun onClose() {
        listener.onClose()
    }

    @JavascriptInterface
    fun onUploadsCreated(uploads: String?, userId: String) {
        listener.onUploadsCreated(uploads, userId)
    }

    @JavascriptInterface
    fun onAccountCreated(accountId: String, userId: String, providerId: String) {
        listener.onAccountCreated(accountId, userId, providerId)
    }

    @JavascriptInterface
    fun onTokenExpired(updateToken: String) {
        listener.onTokenExpired(updateToken)
    }
}

interface SmileBridgeInterface {
    fun onAccountCreated(accountId: String, userId: String, providerId: String)
    fun onAccountConnected(accountId: String, userId: String, providerId: String)
    fun onAccountRemoved(accountId: String, userId: String, providerId: String)
    fun onClose()
    fun onUploadsCreated(uploads: String?, userId: String)
    fun onTokenExpired(updateToken: String)
}