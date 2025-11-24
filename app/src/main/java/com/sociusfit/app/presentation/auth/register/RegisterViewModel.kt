package com.sociusfit.app.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.usecase.auth.LoginWithOAuthUseCase
import com.sociusfit.app.domain.usecase.auth.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel per la schermata di registrazione
 */
class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val loginWithOAuthUseCase: LoginWithOAuthUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onFirstNameChanged(firstName: String) {
        _uiState.update {
            it.copy(
                firstName = firstName,
                firstNameError = null
            )
        }
    }

    fun onLastNameChanged(lastName: String) {
        _uiState.update {
            it.copy(
                lastName = lastName,
                lastNameError = null
            )
        }
    }

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

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update {
            it.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = null
            )
        }
    }

    fun onRegisterClick() {
        val currentState = _uiState.value

        // Clear previous errors
        _uiState.update {
            it.copy(
                firstNameError = null,
                lastNameError = null,
                emailError = null,
                passwordError = null,
                confirmPasswordError = null,
                error = null
            )
        }

        // Validate confirm password match
        if (currentState.password != currentState.confirmPassword) {
            _uiState.update {
                it.copy(confirmPasswordError = "Le password non coincidono")
            }
            return
        }

        // Set loading
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            Timber.d("Starting registration process")

            val result = registerUseCase(
                firstName = currentState.firstName,
                lastName = currentState.lastName,
                email = currentState.email,
                password = currentState.password,
                confirmPassword = currentState.confirmPassword
            )

            when (result) {
                is Result.Success -> {
                    Timber.i("Registration successful")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRegistrationSuccessful = true
                        )
                    }
                }

                is Result.Error -> {
                    Timber.w("Registration failed: ${result.message}")
                    handleRegistrationError(result.message)
                }

                else -> {
                    // Handle any other result type
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Errore sconosciuto durante la registrazione"
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

    private fun handleRegistrationError(errorMessage: String) {
        // Mappa gli errori ai campi appropriati
        when {
            errorMessage.contains("first name", ignoreCase = true) -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        firstNameError = errorMessage
                    )
                }
            }

            errorMessage.contains("last name", ignoreCase = true) -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        lastNameError = errorMessage
                    )
                }
            }

            errorMessage.contains("email", ignoreCase = true) -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        emailError = errorMessage
                    )
                }
            }

            errorMessage.contains("password", ignoreCase = true) &&
                    !errorMessage.contains("match", ignoreCase = true) -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        passwordError = errorMessage
                    )
                }
            }

            errorMessage.contains("match", ignoreCase = true) -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        confirmPasswordError = errorMessage
                    )
                }
            }

            else -> {
                // Errore generico
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
 * UI State per la schermata di registrazione
 */
data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegistrationSuccessful: Boolean = false
)