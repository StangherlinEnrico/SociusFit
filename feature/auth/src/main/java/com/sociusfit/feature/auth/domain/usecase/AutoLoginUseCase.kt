package com.sociusfit.feature.auth.domain.usecase

import com.sociusfit.core.domain.Result
import com.sociusfit.core.domain.model.User
import com.sociusfit.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Auto Login Use Case
 *
 * Gestisce il tentativo di auto-login quando l'app viene aperta.
 * Verifica se esiste un token salvato e se è ancora valido.
 */
class AutoLoginUseCase(
    private val authRepository: AuthRepository
) {
    companion object {
        private const val TAG = "AutoLoginUseCase"
    }

    suspend operator fun invoke(): Result<User> = withContext(Dispatchers.IO) {
        Timber.tag(TAG).d("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        Timber.tag(TAG).d("Executing AutoLoginUseCase")

        try {
            // Verifica se esiste un token salvato
            Timber.tag(TAG).d("Checking if authenticated...")
            val isAuthenticated = authRepository.isAuthenticated()
            Timber.tag(TAG).d("Is authenticated: $isAuthenticated")

            if (!isAuthenticated) {
                Timber.tag(TAG).w("No token found in storage")
                Timber.tag(TAG).d("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                return@withContext Result.Error(
                    exception = IllegalStateException("No token saved")
                )
            }

            val token = authRepository.getToken()
            Timber.tag(TAG).d("Token found: ${token?.take(20)}...")

            // Tenta di recuperare l'utente corrente con il token salvato
            Timber.tag(TAG).d("Calling repository.getCurrentUser()...")
            val result = authRepository.getCurrentUser()

            when (result) {
                is Result.Success -> {
                    Timber.tag(TAG).d("✓ Repository returned SUCCESS")
                    Timber.tag(TAG).d("  User: ${result.data.email}")
                }
                is Result.Error -> {
                    Timber.tag(TAG).w("✗ Repository returned ERROR")
                    Timber.tag(TAG).w("  ${result.exception.message}")
                }
                is Result.Loading -> {
                    Timber.tag(TAG).w("⚠ Repository returned LOADING")
                }
            }

            Timber.tag(TAG).d("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            result

        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Exception in AutoLoginUseCase")
            Timber.tag(TAG).d("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            Result.Error(exception = e)
        }
    }
}