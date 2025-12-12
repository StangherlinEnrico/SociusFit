package com.sociusfit.feature.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.core.domain.Result
import com.sociusfit.feature.auth.domain.usecase.LoginUseCase
import com.sociusfit.feature.auth.domain.usecase.ValidateEmailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Login ViewModel - FIXED
 *
 * Gestisce lo stato e la logica della schermata di login.
 * Dopo login, controlla profileComplete per decidere la navigazione:
 * - profileComplete = true -> Home/Profile
 * - profileComplete = false -> Onboarding
 */
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Event channel per navigazione one-time
    private val _navigationEvent = MutableStateFlow<LoginNavigationEvent?>(null)
    val navigationEvent: StateFlow<LoginNavigationEvent?> = _navigationEvent.asStateFlow()

    /**
     * Aggiorna il campo email con validazione real-time
     */
    fun onEmailChanged(email: String) {
        val (isValid, errorMessage) = validateEmailUseCase(email)

        _uiState.update { state ->
            state.copy(
                email = email,
                isEmailValid = isValid,
                emailError = errorMessage,
                error = null
            )
        }
    }

    /**
     * Aggiorna il campo password
     */
    fun onPasswordChanged(password: String) {
        _uiState.update { state ->
            state.copy(
                password = password,
                error = null
            )
        }
    }

    /**
     * Gestisce il click sul pulsante Login
     *
     * IMPORTANTE: Controlla profileComplete per decidere navigazione:
     * - true -> Profile Screen (utente ha già completato onboarding)
     * - false -> Onboarding (utente deve completare profilo)
     */
    fun onLoginClick() {
        val state = _uiState.value

        Timber.tag(TAG).d("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        Timber.tag(TAG).d("onLoginClick called")

        // Verifica form valido
        if (!state.isFormValid) {
            Timber.tag(TAG).w("Form validation failed")
            _uiState.update { it.copy(error = "Compila tutti i campi correttamente") }
            return
        }

        // Avvia login
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            Timber.tag(TAG).d("Calling LoginUseCase...")
            Timber.tag(TAG).d("  Email: ${state.email}")

            when (val result = loginUseCase(
                email = state.email,
                password = state.password
            )) {
                is Result.Success -> {
                    val user = result.data
                    Timber.tag(TAG).d("✓ Login SUCCESS")
                    Timber.tag(TAG).d("  User ID: ${user.id}")
                    Timber.tag(TAG).d("  Email: ${user.email}")
                    Timber.tag(TAG).d("  Profile Complete: ${user.profileComplete}")

                    // IMPORTANTE: Controlla profileComplete per decidere navigazione
                    val navigationEvent = if (user.profileComplete) {
                        Timber.tag(TAG).d("→ Profile complete: Navigating to PROFILE")
                        LoginNavigationEvent.NavigateToProfile
                    } else {
                        Timber.tag(TAG).d("→ Profile incomplete: Navigating to ONBOARDING")
                        LoginNavigationEvent.NavigateToOnboarding
                    }

                    _uiState.update { it.copy(isLoading = false) }
                    _navigationEvent.value = navigationEvent
                }

                is Result.Error -> {
                    Timber.tag(TAG).e("✗ Login FAILED")
                    Timber.tag(TAG).e("  Error: ${result.exception.message}")

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Errore durante il login"
                        )
                    }
                }

                is Result.Loading -> {
                    // Stato già gestito
                }
            }
        }
    }

    /**
     * Consuma l'evento di navigazione
     */
    fun onNavigationEventConsumed() {
        _navigationEvent.value = null
    }

    /**
     * Dismisses l'errore corrente
     */
    fun onErrorDismissed() {
        _uiState.update { it.copy(error = null) }
    }
}

/**
 * Eventi di navigazione one-time
 */
sealed interface LoginNavigationEvent {
    data object NavigateToProfile : LoginNavigationEvent
    data object NavigateToOnboarding : LoginNavigationEvent
}