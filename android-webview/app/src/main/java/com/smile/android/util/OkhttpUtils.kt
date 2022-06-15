package com.smile.android.util

import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

class OkhttpUtils {
    companion object{
        private var instance:OkhttpUtils? = null
        private var okhttp: OkHttpClient? = null

        @Synchronized
        fun getInstance():OkhttpUtils{
            if (instance == null) instance = OkhttpUtils()
            okhttp = OkHttpClient().newBuilder()
                .retryOnConnectionFailure(true)
                .connectTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(10000, TimeUnit.SECONDS)
                .writeTimeout(10000, TimeUnit.SECONDS)
                .build()
            return instance!!
        }
    }
    fun getRequest(url:String,httpCallBak:HttpCallBack){
        val request: Request = Request.Builder().url(url).build()
        val call: Call? = okhttp?.newCall(request)
        call?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                httpCallBak.onError(e.printStackTrace().toString())
            }
            override fun onResponse(call: Call, response: Response) {
                response.body?.let { httpCallBak.onSuccess(it.string()) }
            }
        })
    }


    fun postRequest(map:HashMap<String,String>,url:String,httpCallBak:HttpCallBack) {
        val builder: FormBody.Builder = FormBody.Builder();
        if (map.size > 0) {
            for (key in map.keys) {
                key.let {
                    builder.add(it, map[it].toString())
                }
            }
        }
        val formBody: FormBody = builder.build()
        val request: Request = Request.Builder().url(url)
            .post(formBody)
            .build()
        val call: Call? = okhttp?.newCall(request)
        call?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                httpCallBak.onError(e.printStackTrace().toString())
            }
            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    httpCallBak.onSuccess(it.string())
                }
            }

        })
    }

}