package com.sociusfit.app.data.remote.interceptor

import com.sociusfit.app.data.local.DataStoreManager
import com.sociusfit.app.data.remote.api.AuthApiService
import com.sociusfit.app.data.remote.api.RefreshTokenRequestDto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

/**
 * 🔥 FIXED: Interceptor per gestire il refresh automatico del token
 *
 * Quando riceve un 401 Unauthorized con header "Token-Expired: true":
 * 1. Prova a fare refresh del token
 * 2. Riprova la richiesta originale con il nuovo token
 * 3. Se fallisce, propaga l'errore (logout necessario)
 */
class TokenRefreshInterceptor(
    private val dataStoreManager: DataStoreManager,
    private val authApiService: AuthApiService
) : Interceptor {

    // Lock per evitare multiple refresh contemporanee
    private val refreshLock = Any()
    private var isRefreshing = false

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        // Controlla se il token è scaduto (401 + header Token-Expired)
        if (response.code == 401) {
            val tokenExpired = response.header("Token-Expired") == "true"

            if (tokenExpired) {
                Timber.w("Token expired, attempting automatic refresh")
                response.close()

                synchronized(refreshLock) {
                    // Se già in refresh, aspetta
                    if (isRefreshing) {
                        Timber.d("Refresh already in progress, waiting...")
                        return response
                    }

                    isRefreshing = true

                    try {
                        val refreshToken = runBlocking {
                            dataStoreManager.refreshToken.first()
                        }

                        if (refreshToken.isNullOrEmpty()) {
                            Timber.e("No refresh token available, cannot refresh")
                            isRefreshing = false
                            return response
                        }

                        // Tenta il refresh
                        val newTokens = runBlocking {
                            attemptTokenRefresh(refreshToken)
                        }

                        if (newTokens != null) {
                            Timber.i("Token refreshed successfully, retrying original request")

                            // Salva i nuovi token
                            runBlocking {
                                dataStoreManager.updateAccessToken(newTokens.accessToken)
                                newTokens.newRefreshToken?.let {
                                    dataStoreManager.updateRefreshToken(it)
                                }
                            }

                            // Riprova la richiesta originale con il nuovo token
                            val newRequest = originalRequest.newBuilder()
                                .header("Authorization", "Bearer ${newTokens.accessToken}")
                                .build()

                            isRefreshing = false
                            return chain.proceed(newRequest)
                        } else {
                            Timber.e("Token refresh failed")
                            // Clear dei token per forzare logout
                            runBlocking {
                                dataStoreManager.clearAll()
                            }
                            isRefreshing = false
                            return response
                        }
                    } catch (e: Exception) {
                        Timber.e(e, "Exception during token refresh")
                        isRefreshing = false
                        return response
                    }
                }
            } else {
                Timber.w("Received 401 but token not expired (invalid credentials or other error)")
            }
        }

        return response
    }

    /**
     * 🔥 Tenta di fare refresh del token
     */
    private suspend fun attemptTokenRefresh(refreshToken: String): RefreshResult? {
        return try {
            Timber.d("Calling refresh token endpoint")

            val request = RefreshTokenRequestDto(refreshToken = refreshToken)
            val response = authApiService.refreshToken(request)

            if (response.isSuccessful && response.body()?.success == true) {
                val authResponse = response.body()?.data

                if (authResponse != null) {
                    Timber.i("Refresh token successful")
                    RefreshResult(
                        accessToken = authResponse.token,
                        newRefreshToken = authResponse.refreshToken
                    )
                } else {
                    Timber.w("Refresh response data is null")
                    null
                }
            } else {
                val errorMessage = response.body()?.message ?: "Unknown error"
                Timber.w("Refresh token failed: $errorMessage")
                null
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception during refresh token call")
            null
        }
    }
}

/**
 * Risultato del refresh token
 */
private data class RefreshResult(
    val accessToken: String,
    val newRefreshToken: String?
)