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
 * Splash ViewModel
 *
 * Gestisce la logica della Splash Screen.
 * Verifica l'autenticazione e decide la navigazione iniziale.
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

                        val destination = if (user.profileComplete) {
                            Timber.tag(TAG).d("→ Navigating to HOME")
                            SplashDestination.Home
                        } else {
                            Timber.tag(TAG).d("→ Navigating to ONBOARDING")
                            SplashDestination.Onboarding
                        }

                        _destination.value = destination
                    }

                    is Result.Error -> {
                        Timber.tag(TAG).w("✗ Auto-login FAILED")
                        Timber.tag(TAG).w("  Error: ${result.exception.message}")
                        Timber.tag(TAG).w("  Cause: ${result.exception.cause?.message}")
                        Timber.tag(TAG).d("→ Navigating to LOGIN")

                        _destination.value = SplashDestination.Login
                    }

                    is Result.Loading -> {
                        Timber.tag(TAG).w("⚠ Unexpected Loading state")
                    }
                }

            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "Exception during authentication check")
                Timber.tag(TAG).d("→ Navigating to LOGIN (fallback)")
                _destination.value = SplashDestination.Login
            }

            Timber.tag(TAG).d("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        }
    }

    /**
     * Resetta la destinazione dopo che la navigazione è stata gestita
     */
    fun onNavigationHandled() {
        Timber.tag(TAG).d("Navigation handled, resetting destination")
        _destination.value = null
    }
}

/**
 * Sealed interface per le destinazioni possibili dalla Splash
 */
sealed interface SplashDestination {
    data object Login : SplashDestination
    data object Onboarding : SplashDestination
    data object Home : SplashDestination
}