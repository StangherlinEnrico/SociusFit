package com.sociusfit.feature.auth.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.core.domain.Result
import com.sociusfit.feature.auth.domain.usecase.AutoLoginUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Splash ViewModel - FIXED
 *
 * Gestisce la logica della Splash Screen.
 * Verifica l'autenticazione e decide la navigazione iniziale:
 *
 * - Nessun token -> Login
 * - Token valido + profileComplete = true -> Profile
 * - Token valido + profileComplete = false -> Onboarding
 * - Token expired/invalido -> Login
 */
class SplashViewModel(
    private val autoLoginUseCase: AutoLoginUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "SplashViewModel"
        private const val SPLASH_DELAY_MS = 2000L
    }

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination.asStateFlow()

    init {
        Timber.tag(TAG).d("ViewModel initialized")
        checkAuthentication()
    }

    /**
     * Verifica lo stato di autenticazione
     *
     * FLUSSO CORRETTO:
     * 1. Aspetta splash delay (2 secondi)
     * 2. Chiama AutoLoginUseCase
     * 3. Se Success:
     *    - profileComplete = true -> Profile
     *    - profileComplete = false -> Onboarding
     * 4. Se Error (no token/expired):
     *    - Login
     */
    private fun checkAuthentication() {
        Timber.tag(TAG).d("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        Timber.tag(TAG).d("Starting authentication check")

        viewModelScope.launch {
            try {
                // Splash minimo 2 secondi
                Timber.tag(TAG).d("Waiting splash delay (${SPLASH_DELAY_MS}ms)...")
                delay(SPLASH_DELAY_MS)
                Timber.tag(TAG).d("Splash delay completed")

                // Tenta auto-login
                Timber.tag(TAG).d("Calling AutoLoginUseCase...")
                val result = autoLoginUseCase()
                Timber.tag(TAG).d("AutoLoginUseCase returned: ${result::class.simpleName}")

                when (result) {
                    is Result.Success -> {
                        val user = result.data
                        Timber.tag(TAG).d("✓ Auto-login SUCCESS")
                        Timber.tag(TAG).d("  User ID: ${user.id}")
                        Timber.tag(TAG).d("  Email: ${user.email}")
                        Timber.tag(TAG).d("  Profile Complete: ${user.profileComplete}")

                        // IMPORTANTE: Controlla profileComplete per decidere navigazione
                        val destination = if (user.profileComplete) {
                            Timber.tag(TAG).d("→ Profile complete: Navigating to PROFILE")
                            SplashDestination.Profile
                        } else {
                            Timber.tag(TAG).d("→ Profile incomplete: Navigating to ONBOARDING")
                            SplashDestination.Onboarding
                        }

                        _destination.value = destination
                    }

                    is Result.Error -> {
                        Timber.tag(TAG).w("✗ Auto-login FAILED")
                        Timber.tag(TAG).w("  Error: ${result.exception.message}")
                        Timber.tag(TAG).w("  Cause: ${result.exception.cause?.message}")
                        Timber.tag(TAG).d("→ No valid token: Navigating to LOGIN")

                        _destination.value = SplashDestination.Login
                    }

                    is Result.Loading -> {
                        // Non dovrebbe succedere da AutoLoginUseCase
                        Timber.tag(TAG).w("Unexpected Loading result")
                    }
                }

                Timber.tag(TAG).d("Authentication check completed")
                Timber.tag(TAG).d("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "Exception during authentication check")
                _destination.value = SplashDestination.Login
            }
        }
    }

    /**
     * Reset destination dopo navigazione
     */
    fun onNavigationComplete() {
        _destination.value = null
    }
}

/**
 * Destinazioni possibili dalla Splash Screen
 *
 * FLUSSO CORRETTO:
 * - Login: Nessun token o token invalido
 * - Onboarding: Token valido MA profilo non completato
 * - Profile: Token valido E profilo completato
 */
sealed interface SplashDestination {
    data object Login : SplashDestination
    data object Onboarding : SplashDestination
    data object Profile : SplashDestination
}