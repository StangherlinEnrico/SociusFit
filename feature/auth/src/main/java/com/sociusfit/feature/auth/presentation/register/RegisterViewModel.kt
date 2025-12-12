package com.sociusfit.feature.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.core.domain.Result
import com.sociusfit.feature.auth.domain.usecase.RegisterUseCase
import com.sociusfit.feature.auth.domain.usecase.ValidateEmailUseCase
import com.sociusfit.feature.auth.domain.usecase.ValidatePasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Register ViewModel - FIXED
 *
 * Gestisce lo stato e la logica della schermata di registrazione.
 * Dopo registrazione, naviga SEMPRE all'onboarding (profileComplete = false)
 */
class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "RegisterViewModel"
    }

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // Event channel per navigazione one-time
    private val _navigationEvent = MutableStateFlow<RegisterNavigationEvent?>(null)
    val navigationEvent: StateFlow<RegisterNavigationEvent?> = _navigationEvent.asStateFlow()

    /**
     * Aggiorna il campo firstName
     */
    fun onFirstNameChanged(firstName: String) {
        _uiState.update { state ->
            state.copy(
                firstName = firstName,
                isFirstNameValid = firstName.length >= 2 || firstName.isEmpty(),
                error = null
            )
        }
    }

    /**
     * Aggiorna il campo lastName
     */
    fun onLastNameChanged(lastName: String) {
        _uiState.update { state ->
            state.copy(
                lastName = lastName,
                isLastNameValid = lastName.length >= 2 || lastName.isEmpty(),
                error = null
            )
        }
    }

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
     * Aggiorna il campo password con validazione real-time
     */
    fun onPasswordChanged(password: String) {
        val (isValid, errorMessage) = validatePasswordUseCase(password)

        _uiState.update { state ->
            state.copy(
                password = password,
                isPasswordValid = isValid,
                passwordError = errorMessage,
                error = null
            )
        }
    }

    /**
     * Gestisce il click sul pulsante Registrati
     *
     * IMPORTANTE: Dopo registrazione SEMPRE -> Onboarding
     * Il backend restituisce profileComplete = false per nuovi utenti
     */
    fun onRegisterClick() {
        val state = _uiState.value

        Timber.tag(TAG).d("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        Timber.tag(TAG).d("onRegisterClick called")

        // Verifica form valido
        if (!state.isFormValid) {
            Timber.tag(TAG).w("Form validation failed")
            _uiState.update { it.copy(error = "Compila tutti i campi correttamente") }
            return
        }

        // Avvia registrazione
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            Timber.tag(TAG).d("Calling RegisterUseCase...")
            Timber.tag(TAG).d("  First Name: ${state.firstName}")
            Timber.tag(TAG).d("  Last Name: ${state.lastName}")
            Timber.tag(TAG).d("  Email: ${state.email}")

            when (val result = registerUseCase(
                firstName = state.firstName,
                lastName = state.lastName,
                email = state.email,
                password = state.password
            )) {
                is Result.Success -> {
                    val user = result.data
                    Timber.tag(TAG).d("✓ Registration SUCCESS")
                    Timber.tag(TAG).d("  User ID: ${user.id}")
                    Timber.tag(TAG).d("  Email: ${user.email}")
                    Timber.tag(TAG).d("  Profile Complete: ${user.profileComplete}")

                    // IMPORTANTE: Dopo registrazione -> SEMPRE Onboarding
                    // Il backend restituisce profileComplete = false
                    Timber.tag(TAG).d("→ Navigating to ONBOARDING")

                    _uiState.update { it.copy(isLoading = false) }
                    _navigationEvent.value = RegisterNavigationEvent.NavigateToOnboarding
                }

                is Result.Error -> {
                    Timber.tag(TAG).e("✗ Registration FAILED")
                    Timber.tag(TAG).e("  Error: ${result.exception.message}")

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Errore durante la registrazione"
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
sealed interface RegisterNavigationEvent {
    data object NavigateToOnboarding : RegisterNavigationEvent
}