package com.sociusfit.app.presentation.auth.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.usecase.auth.ForgotPasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel per la schermata Forgot Password
 */
class ForgotPasswordViewModel(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = null
            )
        }
    }

    fun onSendClick() {
        val currentState = _uiState.value

        // Clear previous errors
        _uiState.update {
            it.copy(
                emailError = null,
                error = null
            )
        }

        // Set loading
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            Timber.d("Starting forgot password request")

            when (val result = forgotPasswordUseCase(currentState.email)) {
                is Result.Success -> {
                    Timber.i("Password reset email sent successfully")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isEmailSent = true,
                            successMessage = result.data
                        )
                    }
                }

                is Result.Error -> {
                    Timber.w("Forgot password failed: ${result.message}")
                    handleError(result.message)
                }

                else -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Errore sconosciuto durante l'invio dell'email"
                        )
                    }
                }
            }
        }
    }

    private fun handleError(errorMessage: String) {
        when {
            errorMessage.contains("email", ignoreCase = true) -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        emailError = errorMessage
                    )
                }
            }

            else -> {
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
 * UI State per la schermata Forgot Password
 */
data class ForgotPasswordUiState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEmailSent: Boolean = false,
    val successMessage: String? = null
)