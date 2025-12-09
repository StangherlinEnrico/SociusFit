package com.sociusfit.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

/**
 * Error Handling Interceptor
 *
 * Logga gli errori HTTP ma NON lancia exception.
 * Le exception vengono gestite dal Repository layer.
 */
class ErrorHandlingInterceptor : Interceptor {

    companion object {
        private const val TAG = "ErrorHandling"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // Logga errori HTTP per debugging
        if (!response.isSuccessful) {
            val code = response.code
            val message = response.message
            val body = response.peekBody(Long.MAX_VALUE).string()

            Timber.tag(TAG).w("HTTP Error $code: $message")
            Timber.tag(TAG).w("URL: ${request.url}")
            Timber.tag(TAG).w("Body: $body")

            // Log specifici per codici comuni
            when (code) {
                401 -> Timber.tag(TAG).w("Unauthorized - Check credentials or token")
                403 -> Timber.tag(TAG).w("Forbidden - Insufficient permissions")
                404 -> Timber.tag(TAG).w("Not Found - Check endpoint URL")
                500 -> Timber.tag(TAG).e("Internal Server Error")
            }
        }

        // Ritorna la response SENZA lanciare exception
        // Il repository gestirà gli errori HTTP
        return response
    }
}