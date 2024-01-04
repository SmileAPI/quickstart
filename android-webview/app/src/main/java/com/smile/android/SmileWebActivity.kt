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
import com.smile.android.entity.AccountModel
import com.smile.android.entity.ArchiveModel
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
        /**
         * Get data from the network without caching.
         * Recommended to turn off caching.
         */
        wv.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        /**
         * Wink SDK and Kotlin communication mode
         * Wink SDK call Kotlin
         * Set communication bridge class
         */
        wv.addJavascriptInterface(SmileJsCallBack(this), "smile")

        with(OkhttpUtils) {
            /**
             * Call the node server to get user token.
             * This process will get the relevant SDK initialization data from the backend in your code.
             */
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
                            val jsModel = SmileJsModel(
                                /**
                                 * Use provider id to filter provider list. Example ['upwork', 'freelancer']
                                 */
                                providers = model.providers,
                                /**
                                 * User token passed from your backend service which is obtained from the Smile API.
                                 */
                                userToken = model.accessToken,
                                /**
                                 * Enable or disable file uploads.
                                 */
                                enableUpload = model.enableUpload,
                                /**
                                 * Use provider id to filter provider list. Example ['upwork', 'freelancer']
                                 */
                                topProviders = model.topProviders)
                            Log.d("initParam",Gson().toJson(jsModel))
                            runOnUiThread {wv.loadUrl("file:///android_asset/smile.html?initParam=${Gson().toJson(jsModel)}")}
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
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        /**
         * Archive upload file function related configurations.
         */
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
        //TODO("Relevant business code after account created")
    }


    override fun onAccountConnected(accountId: String, userId: String, providerId: String) {
        Log.d("accountId", accountId)
        Log.d("userId", userId)
        Log.d("providerId", providerId)
        //TODO("Relevant business code after account connected")
    }

    override fun onAccountRemoved(accountId: String, userId: String, providerId: String) {
        Log.d("accountId", accountId)
        Log.d("userId", userId)
        Log.d("providerId", providerId)
        //TODO("Relevant business code after account removed")
    }

    override fun onClose() {
        Log.d("onClose", "")
        //TODO("Relevant business code after sdk close")
    }

    override fun onUploadsCreated(uploads: String?, userId: String) {
        Log.d("uploads", uploads.toString())
        Log.d("userId", userId)
        //TODO("Relevant business code after upload")
    }

     override fun onUploadsRemoved(uploads: String?, userId: String) {
        Log.d("uploads", uploads.toString())
        Log.d("userId", userId)
        //TODO("Relevant business code after upload")
    }

    override fun onTokenExpired() {
        Log.d("token expired","")
        //TODO("Relevant business code if then token expired")
    }

    override fun onAccountError(accountId: String, userId: String, providerId: String, errorCode: String) {
        Log.d("accountId", accountId)
        Log.d("userId", userId)
        Log.d("providerId", providerId)
        Log.d("errorCode", errorCode)
        //TODO("Relevant business code if account connected error")
    }

    override fun onUIEvent(eventName: String, eventTime: String, mode: String, userId: String?, account: String?, archive:String?) {
        Log.d("eventName", eventName)
        Log.d("eventTime", eventTime)
        Log.d("mode", mode)
        Log.d("userId", userId.toString())

        if (account != null) {
            Log.d("account", account.toString())
        }
        if (archive != null) {
            Log.d("archive", archive.toString())
        }
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
    fun onUploadsRemoved(uploads: String?, userId: String) {
        listener.onUploadsRemoved(uploads, userId)
    }

    @JavascriptInterface
    fun onAccountCreated(accountId: String, userId: String, providerId: String) {
        listener.onAccountCreated(accountId, userId, providerId)
    }

    @JavascriptInterface
    fun onTokenExpired() {
        listener.onTokenExpired()
    }

    @JavascriptInterface
    fun onAccountError(accountId: String, userId: String, providerId: String, errorCode: String ) {
        listener.onAccountError(accountId, userId, providerId, errorCode)
    }

    @JavascriptInterface
    fun onUIEvent(eventName: String, eventTime: String, mode: String, userId: String?, account: String?, archive:String?){
        listener.onUIEvent(eventName, eventTime, mode, userId, account, archive)
    }
}

/**
 * kotlin listener javascript interface
 */
interface SmileBridgeInterface {
    fun onAccountCreated(accountId: String, userId: String, providerId: String)
    fun onAccountConnected(accountId: String, userId: String, providerId: String)
    fun onAccountRemoved(accountId: String, userId: String, providerId: String)
    fun onAccountError(accountId: String, userId: String, providerId: String, errorCode: String)
    fun onClose()
    fun onUploadsCreated(uploads: String?, userId: String)
    fun onUploadsRemoved(uploads: String?, userId: String)
    fun onTokenExpired()
    fun onUIEvent(eventName: String, eventTime: String, mode: String, userId: String?, account: String?, archive:String?)
}