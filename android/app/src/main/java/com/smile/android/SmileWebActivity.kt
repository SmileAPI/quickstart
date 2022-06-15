package com.smile.android

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        Log.d("TestActivityTag", "init")
        val wv = findViewById<WebView>(R.id.wv_smil_view)
        wv.settings.javaScriptEnabled = true
        wv.settings.allowContentAccess = true
        wv.settings.javaScriptCanOpenWindowsAutomatically = true
        wv.settings.allowFileAccess = true
        wv.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        wv.settings.allowFileAccessFromFileURLs = true
        wv.addJavascriptInterface(SmileJsCallBack(this), "smile")

        with(OkhttpUtils) {
            getInstance().getRequest(
                "http://192.168.1.3:8000/api/create_link_token",
                object : HttpCallBack {
                    override fun onError(errorLog: String) {
                        Log.e("errorLog", errorLog)
                    }
                    override fun onSuccess(message: String) {
                        Log.d("message", message)
                        try {
                            val type = object : TypeToken<ResultModel>() {}.type
                            val model: ResultModel =  Gson().fromJson(message, type)
                            val filterList = emptyList<String>()
                            val topProviders = emptyList<String>()
                            val enableUpload = true
                            val linkUrl = model.apiHost
                            val accessToken = model.accessToken
                            val jsModel = SmileJsModel(
                                apiHost = linkUrl, providers = filterList,
                                userToken = accessToken, enableUpload = enableUpload, topProviders = topProviders
                            )
                            runOnUiThread {wv.loadUrl("file:///android_asset/smile.html?data=${Gson().toJson(jsModel)}") }
                        } catch (e: Exception) {
                            println("try exception,${e.message}")
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
}

interface SmileBridgeInterface {
    fun onAccountConnected(accountId: String, userId: String, providerId: String)
    fun onAccountRemoved(accountId: String, userId: String, providerId: String)
    fun onClose()
    fun onUploadsCreated(uploads: String?, userId: String)
}