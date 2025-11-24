package com.sociusfit.app.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.usecase.auth.LoginUseCase
import com.sociusfit.app.domain.usecase.auth.LoginWithOAuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel per la schermata di login
 */
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val loginWithOAuthUseCase: LoginWithOAuthUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = null
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = null
            )
        }
    }

    fun onLoginClick() {
        val currentState = _uiState.value

        // Clear previous errors
        _uiState.update {
            it.copy(
                emailError = null,
                passwordError = null,
                error = null
            )
        }

        // Set loading
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            Timber.d("Starting login process")

            val result = loginUseCase(
                email = currentState.email,
                password = currentState.password
            )

            when (result) {
                is Result.Success -> {
                    Timber.i("Login successful")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoginSuccessful = true
                        )
                    }
                }

                is Result.Error -> {
                    Timber.w("Login failed: ${result.message}")
                    handleLoginError(result.message)
                }

                else -> {
                    // Handle any other result type
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Errore sconosciuto durante il login"
                        )
                    }
                }
            }
        }
    }

    fun onGoogleSignInClick() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            Timber.d("Starting Google Sign-In")

            // TODO: Implementare Google Sign-In SDK
            // Per ora mostra solo un messaggio
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = "Google Sign-In non ancora implementato"
                )
            }
        }
    }

    fun onAppleSignInClick() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            Timber.d("Starting Apple Sign-In")

            // TODO: Implementare Apple Sign-In
            // Per ora mostra solo un messaggio
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = "Apple Sign-In non ancora implementato"
                )
            }
        }
    }

    private fun handleLoginError(errorMessage: String) {
        // Mappa gli errori ai campi appropriati
        when {
            errorMessage.contains("email", ignoreCase = true) -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        emailError = errorMessage
                    )
                }
            }

            errorMessage.contains("password", ignoreCase = true) -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        passwordError = errorMessage
                    )
                }
            }

            else -> {
                // Errore generico o credenziali non valide
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

/**
 * UI State per la schermata di login
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoginSuccessful: Boolean = false
)