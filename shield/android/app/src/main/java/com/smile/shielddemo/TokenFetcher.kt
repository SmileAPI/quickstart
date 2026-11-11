package com.smile.shielddemo

import android.os.Handler
import android.os.Looper
import android.util.Log
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Obtains a `userToken` from the quickstart Node server.
 *
 * This mirrors what a real app does on its own backend: call the Smile Open API, keep the
 * secret server-side, and hand the resulting token to the SDK. The default host is the
 * emulator loopback alias `10.0.2.2`; on a physical device pass the machine's LAN IP.
 */
object TokenFetcher {

    /** Emulator → host machine. Replace with your LAN IP when running on a real device. */
    const val DEFAULT_ENDPOINT = "http://10.0.2.2:8000/api/create_link_token"

    fun fetch(endpoint: String = DEFAULT_ENDPOINT, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val main = Handler(Looper.getMainLooper())
        Thread {
            try {
                val connection = (URL(endpoint).openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 10_000
                    readTimeout = 15_000
                }
                try {
                    val body = connection.inputStream.bufferedReader().use { it.readText() }
                    val token = JSONObject(body).optString("accessToken")
                    main.post {
                        if (token.isNullOrBlank()) {
                            onError("accessToken missing in response: $body")
                        } else {
                            onSuccess(token)
                        }
                    }
                } finally {
                    connection.disconnect()
                }
            } catch (e: Exception) {
                Log.e(ShieldJsBridge.TAG, "token request failed", e)
                main.post { onError(e.message ?: "unknown error") }
            }
        }.start()
    }
}
