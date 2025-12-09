package com.sociusfit.feature.auth.presentation.register

import android.util.Log
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

/**
 * Register ViewModel
 *
 * Gestisce lo stato e la logica della schermata di registrazione.
 * Implementa MVVM pattern con unidirectional data flow.
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
     */
    fun onRegisterClick() {
        val state = _uiState.value

        // Verifica form valido
        if (!state.isFormValid) {
            _uiState.update { it.copy(error = "Compila tutti i campi correttamente") }
            return
        }

        // Avvia registrazione
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = registerUseCase(
                firstName = state.firstName,
                lastName = state.lastName,
                email = state.email,
                password = state.password
            )) {
                is Result.Success -> {
                    Log.d(TAG, "Registration successful: ${result.data.id}")
                    // La navigazione sarà gestita dalla UI che osserva questo stato
                    // Il successo viene indicato da isLoading = false e error = null
                    _uiState.update { it.copy(isLoading = false) }
                }

                is Result.Error -> {
                    Log.e(TAG, "Registration failed", result.exception)
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
     * Dismisses l'errore corrente
     */
    fun onErrorDismissed() {
        _uiState.update { it.copy(error = null) }
    }
}