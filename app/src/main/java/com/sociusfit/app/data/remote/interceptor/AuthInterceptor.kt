package com.sociusfit.app.data.remote.interceptor

import com.sociusfit.app.data.local.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

/**
 * OkHttp Interceptor that adds authentication token to requests
 * Automatically adds "Authorization: Bearer {token}" header to all requests
 */
class AuthInterceptor(
    private val dataStoreManager: DataStoreManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Skip adding token for auth endpoints
        val url = originalRequest.url.toString()
        if (shouldSkipAuth(url)) {
            return chain.proceed(originalRequest)
        }

        // Get token from DataStore
        val token = runBlocking {
            try {
                dataStoreManager.authToken.first()
            } catch (e: Exception) {
                Timber.e(e, "Error retrieving auth token")
                null
            }
        }

        // If no token, proceed without auth header
        if (token.isNullOrBlank()) {
            Timber.w("No auth token available for request: ${originalRequest.url}")
            return chain.proceed(originalRequest)
        }

        // Add Authorization header
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        Timber.d("Added auth token to request: ${authenticatedRequest.url}")
        return chain.proceed(authenticatedRequest)
    }

    /**
     * Check if the request should skip authentication
     * Auth endpoints don't need the token
     */
    private fun shouldSkipAuth(url: String): Boolean {
        return url.contains("/login") ||
                url.contains("/register") ||
                url.contains("/oauth")
    }
}