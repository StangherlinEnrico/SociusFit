package com.sociusfit.app.domain.usecase.auth

import com.sociusfit.app.domain.model.AuthResponse
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.repository.AuthRepository
import timber.log.Timber

/**
 * Use case for user registration
 * Handles input validation and business logic
 */
class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Execute registration
     * @param firstName User first name
     * @param lastName User last name
     * @param email User email
     * @param password User password
     * @param confirmPassword Password confirmation
     * @return Result with AuthResponse or error
     */
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<AuthResponse> {
        // Validate first name
        if (firstName.isBlank()) {
            Timber.w("Registration failed: First name is empty")
            return Result.Error("First name cannot be empty")
        }

        if (firstName.length < 2) {
            Timber.w("Registration failed: First name too short")
            return Result.Error("First name must be at least 2 characters")
        }

        // Validate last name
        if (lastName.isBlank()) {
            Timber.w("Registration failed: Last name is empty")
            return Result.Error("Last name cannot be empty")
        }

        if (lastName.length < 2) {
            Timber.w("Registration failed: Last name too short")
            return Result.Error("Last name must be at least 2 characters")
        }

        // Validate email
        if (email.isBlank()) {
            Timber.w("Registration failed: Email is empty")
            return Result.Error("Email cannot be empty")
        }

        if (!isValidEmail(email)) {
            Timber.w("Registration failed: Invalid email format")
            return Result.Error("Invalid email format")
        }

        // Validate password
        if (password.isBlank()) {
            Timber.w("Registration failed: Password is empty")
            return Result.Error("Password cannot be empty")
        }

        if (password.length < 8) {
            Timber.w("Registration failed: Password too short")
            return Result.Error("Password must be at least 8 characters")
        }

        if (password.length > 128) {
            Timber.w("Registration failed: Password too long")
            return Result.Error("Password must be less than 128 characters")
        }

        // Validate password strength
        if (!hasUpperCase(password)) {
            Timber.w("Registration failed: Password needs uppercase")
            return Result.Error("Password must contain at least one uppercase letter")
        }

        if (!hasLowerCase(password)) {
            Timber.w("Registration failed: Password needs lowercase")
            return Result.Error("Password must contain at least one lowercase letter")
        }

        if (!hasDigit(password)) {
            Timber.w("Registration failed: Password needs digit")
            return Result.Error("Password must contain at least one number")
        }

        // Validate password confirmation
        if (password != confirmPassword) {
            Timber.w("Registration failed: Passwords don't match")
            return Result.Error("Passwords do not match")
        }

        // All validations passed, proceed with registration
        Timber.d("Validation passed, proceeding with registration")
        return authRepository.register(
            firstName = firstName.trim(),
            lastName = lastName.trim(),
            email = email.trim(),
            password = password
        )
    }

    /**
     * Basic email validation
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        return email.matches(emailRegex)
    }

    /**
     * Check if password has at least one uppercase letter
     */
    private fun hasUpperCase(password: String): Boolean {
        return password.any { it.isUpperCase() }
    }

    /**
     * Check if password has at least one lowercase letter
     */
    private fun hasLowerCase(password: String): Boolean {
        return password.any { it.isLowerCase() }
    }

    /**
     * Check if password has at least one digit
     */
    private fun hasDigit(password: String): Boolean {
        return password.any { it.isDigit() }
    }
}