package com.sociusfit.feature.auth.presentation.login

/**
 * Login UI State
 *
 * Rappresenta lo stato della UI della schermata di login.
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEmailValid: Boolean = true,
    val emailError: String? = null
) {
    /**
     * Verifica se il form è valido per la submission
     */
    val isFormValid: Boolean
        get() = email.isNotBlank() &&
                password.isNotBlank() &&
                isEmailValid
}