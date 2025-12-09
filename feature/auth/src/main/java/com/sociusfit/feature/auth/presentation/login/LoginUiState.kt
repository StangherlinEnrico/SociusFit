package com.sociusfit.feature.auth.presentation.login

/**
 * Login UI State
 *
 * Rappresenta lo stato della schermata di login.
 * Immutable data class per garantire unidirectional data flow.
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
     * Form valido se email e password sono compilati
     */
    val isFormValid: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && isEmailValid
}