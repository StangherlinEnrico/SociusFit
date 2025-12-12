package com.sociusfit.feature.auth.presentation.register

/**
 * Register UI State
 *
 * Rappresenta lo stato della UI della schermata di registrazione.
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
    val emailError: String? = null,
    val isPasswordValid: Boolean = true,
    val passwordError: String? = null
) {
    /**
     * Verifica se il form è valido per la submission
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