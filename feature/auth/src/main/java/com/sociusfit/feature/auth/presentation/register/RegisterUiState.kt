package com.sociusfit.feature.auth.presentation.register

/**
 * Register UI State
 *
 * Rappresenta lo stato della schermata di registrazione.
 * Immutable data class per garantire unidirectional data flow.
 */
data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFirstNameValid: Boolean = true,
    val isLastNameValid: Boolean = true,
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val emailError: String? = null,
    val passwordError: String? = null
) {
    /**
     * Form valido se tutti i campi sono compilati e validi
     */
    val isFormValid: Boolean
        get() = firstName.isNotBlank() &&
                lastName.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                isFirstNameValid &&
                isLastNameValid &&
                isEmailValid &&
                isPasswordValid
}