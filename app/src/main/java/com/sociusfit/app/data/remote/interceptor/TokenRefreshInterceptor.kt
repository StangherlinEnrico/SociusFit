package com.sociusfit.app.data.remote.interceptor

import com.sociusfit.app.data.local.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

/**
 * Interceptor per gestire il refresh automatico del token
 *
 * Quando riceve un 401 Unauthorized:
 * 1. Prova a fare refresh del token
 * 2. Riprova la richiesta originale con il nuovo token
 * 3. Se fallisce, propaga l'errore (logout necessario)
 */
class TokenRefreshInterceptor(
    private val dataStoreManager: DataStoreManager,
    private val tokenRefreshHelper: TokenRefreshHelper
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // Se riceviamo 401 e abbiamo un refresh token, proviamo a fare refresh
        if (response.code == 401) {
            Timber.w("Received 401 Unauthorized, attempting token refresh")

            synchronized(this) {
                val refreshToken = runBlocking {
                    dataStoreManager.refreshToken.first()
                }

                if (refreshToken.isNullOrEmpty()) {
                    Timber.e("No refresh token available, logout required")
                    return response // Nessun refresh token, propaghiamo il 401
                }

                // Chiudi la response originale
                response.close()

                // Prova a fare refresh
                val refreshResult = runBlocking {
                    tokenRefreshHelper.refreshAccessToken(refreshToken)
                }

                if (refreshResult != null) {
                    Timber.i("Token refreshed successfully, retrying request")

                    // Salva il nuovo token
                    runBlocking {
                        dataStoreManager.saveAuthToken(refreshResult.token)
                        // Se il backend ritorna un nuovo refresh token, salvalo
                        refreshResult.refreshToken?.let {
                            dataStoreManager.saveRefreshToken(it)
                        }
                    }

                    // Riprova la richiesta originale con il nuovo token
                    val newRequest = request.newBuilder()
                        .header("Authorization", "Bearer ${refreshResult.token}")
                        .build()

                    return chain.proceed(newRequest)
                } else {
                    Timber.e("Token refresh failed, logout required")
                    // Refresh fallito, clear dei token
                    runBlocking {
                        dataStoreManager.clearAll()
                    }
                    return response // Propaghiamo il 401
                }
            }
        }

        return response
    }
}

/**
 * Helper per il refresh del token
 * Separato dall'interceptor per evitare dipendenze circolari
 */
class TokenRefreshHelper(
    private val authApiService: com.sociusfit.app.data.remote.api.AuthApiService
) {
    /**
     * Tenta di fare refresh del token
     * @return Nuovo token se successo, null se fallito
     */
    suspend fun refreshAccessToken(refreshToken: String): RefreshTokenResult? {
        return try {
            Timber.d("Attempting to refresh access token")

            val request = com.sociusfit.app.data.remote.api.RefreshTokenRequestDto(
                refreshToken = refreshToken
            )

            val response = authApiService.refreshToken(request)

            if (response.isSuccessful && response.body()?.success == true) {
                val authResponseDto = response.body()?.data

                if (authResponseDto != null) {
                    Timber.i("Access token refreshed successfully")

                    RefreshTokenResult(
                        token = authResponseDto.token,
                        refreshToken = authResponseDto.refreshToken
                    )
                } else {
                    Timber.w("Token refresh response data is null")
                    null
                }
            } else {
                Timber.w("Token refresh failed: ${response.body()?.message}")
                null
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception during token refresh")
            null
        }
    }
}

/**
 * Risultato del refresh token
 */
data class RefreshTokenResult(
    val token: String,
    val refreshToken: String?
)