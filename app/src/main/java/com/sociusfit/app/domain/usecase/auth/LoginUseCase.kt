package com.sociusfit.app.domain.usecase.auth

import com.sociusfit.app.domain.model.AuthResponse
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.repository.AuthRepository
import timber.log.Timber

/**
 * Use case for user login with email and password
 * Handles input validation and business logic
 */
class LoginUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Execute login
     * @param email User email
     * @param password User password
     * @return Result with AuthResponse or error
     */
    suspend operator fun invoke(email: String, password: String): Result<AuthResponse> {
        // Validate email
        if (email.isBlank()) {
            Timber.w("Login failed: Email is empty")
            return Result.Error("Email cannot be empty")
        }

        if (!isValidEmail(email)) {
            Timber.w("Login failed: Invalid email format")
            return Result.Error("Invalid email format")
        }

        // Validate password
        if (password.isBlank()) {
            Timber.w("Login failed: Password is empty")
            return Result.Error("Password cannot be empty")
        }

        if (password.length < 8) {
            Timber.w("Login failed: Password too short")
            return Result.Error("Password must be at least 8 characters")
        }

        // All validations passed, proceed with login
        Timber.d("Validation passed, proceeding with login")
        return authRepository.login(email.trim(), password)
    }

    /**
     * Basic email validation
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        return email.matches(emailRegex)
    }
}