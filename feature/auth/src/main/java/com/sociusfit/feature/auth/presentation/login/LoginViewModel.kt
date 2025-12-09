package com.sociusfit.feature.auth.presentation.login

import android.util.Log
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

/**
 * Login ViewModel
 *
 * Gestisce lo stato e la logica della schermata di login.
 * Implementa MVVM pattern con unidirectional data flow.
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
     */
    fun onLoginClick() {
        val state = _uiState.value

        // Verifica form valido
        if (!state.isFormValid) {
            _uiState.update { it.copy(error = "Compila tutti i campi correttamente") }
            return
        }

        // Avvia login
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = loginUseCase(
                email = state.email,
                password = state.password
            )) {
                is Result.Success -> {
                    Log.d(TAG, "Login successful: ${result.data.id}")
                    // La navigazione sarà gestita dalla UI che osserva questo stato
                    _uiState.update { it.copy(isLoading = false) }
                }

                is Result.Error -> {
                    Log.e(TAG, "Login failed", result.exception)
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
     * Dismisses l'errore corrente
     */
    fun onErrorDismissed() {
        _uiState.update { it.copy(error = null) }
    }
}