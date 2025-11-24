package com.sociusfit.app.domain.usecase.auth

import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.repository.AuthRepository
import timber.log.Timber

/**
 * Use case for requesting password reset
 * Handles email validation and sends reset email
 */
class ForgotPasswordUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Execute forgot password request
     * @param email User email to send reset link
     * @return Result indicating success or error with message
     */
    suspend operator fun invoke(email: String): Result<String> {
        // Validate email
        if (email.isBlank()) {
            Timber.w("Forgot password failed: Email is empty")
            return Result.Error("Email cannot be empty")
        }

        if (!isValidEmail(email)) {
            Timber.w("Forgot password failed: Invalid email format")
            return Result.Error("Invalid email format")
        }

        // All validations passed, proceed with forgot password request
        Timber.d("Validation passed, proceeding with forgot password request")
        return authRepository.forgotPassword(email.trim())
    }

    /**
     * Basic email validation
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        return email.matches(emailRegex)
    }
}