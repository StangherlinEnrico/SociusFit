package com.sociusfit.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber
import java.io.IOException

/**
 * Logging Interceptor
 *
 * Logga tutte le richieste e risposte HTTP per debugging.
 * Usa Timber per logging strutturato.
 */
class LoggingInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Log Request
        Timber.tag("HTTP").d("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        Timber.tag("HTTP").d("→ REQUEST")
        Timber.tag("HTTP").d("→ Method: ${request.method}")
        Timber.tag("HTTP").d("→ URL: ${request.url}")
        Timber.tag("HTTP").d("→ Headers: ${request.headers}")

        // Log Request Body se presente
        request.body?.let { body ->
            val buffer = okio.Buffer()
            body.writeTo(buffer)
            Timber.tag("HTTP").d("→ Body: ${buffer.readUtf8()}")
        }

        // Esegui richiesta e misura tempo
        val startTime = System.currentTimeMillis()
        val response: Response

        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            Timber.tag("HTTP").e(e, "✗ REQUEST FAILED")
            throw e
        }

        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        // Log Response
        Timber.tag("HTTP").d("← RESPONSE (${duration}ms)")
        Timber.tag("HTTP").d("← Code: ${response.code}")
        Timber.tag("HTTP").d("← Message: ${response.message}")
        Timber.tag("HTTP").d("← Headers: ${response.headers}")

        // Log Response Body (preservando il body per il consumatore)
        val responseBody = response.body
        val source = responseBody?.source()
        source?.request(Long.MAX_VALUE)
        val buffer = source?.buffer
        val bodyString = buffer?.clone()?.readUtf8() ?: ""

        if (bodyString.isNotEmpty()) {
            Timber.tag("HTTP").d("← Body: $bodyString")
        }

        // Ricrea response con body intatto
        val newBody = bodyString.toResponseBody(responseBody?.contentType())
        val newResponse = response.newBuilder()
            .body(newBody)
            .build()

        Timber.tag("HTTP").d("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

        return newResponse
    }
}