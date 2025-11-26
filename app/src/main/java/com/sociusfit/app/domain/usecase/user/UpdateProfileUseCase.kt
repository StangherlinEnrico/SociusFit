package com.sociusfit.app.domain.usecase.user

import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.model.User
import com.sociusfit.app.domain.repository.UserRepository
import timber.log.Timber

/**
 * Use case for updating user profile
 * Handles validation and business logic for profile updates
 */
class UpdateProfileUseCase(
    private val userRepository: UserRepository
) {
    /**
     * Execute profile update
     * @param firstName User first name
     * @param lastName User last name
     * @param location User location description (optional)
     * @return Result with updated User or error
     */
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        location: String?,
        maxDistance: Int
    ): Result<User> {
        // Validate first name
        if (firstName.isBlank()) {
            Timber.w("Update profile failed: First name is empty")
            return Result.Error("First name cannot be empty")
        }

        if (firstName.length < 2) {
            Timber.w("Update profile failed: First name too short")
            return Result.Error("First name must be at least 2 characters")
        }

        if (firstName.length > 100) {
            Timber.w("Update profile failed: First name too long")
            return Result.Error("First name must be less than 100 characters")
        }

        // Validate last name
        if (lastName.isBlank()) {
            Timber.w("Update profile failed: Last name is empty")
            return Result.Error("Last name cannot be empty")
        }

        if (lastName.length < 2) {
            Timber.w("Update profile failed: Last name too short")
            return Result.Error("Last name must be at least 2 characters")
        }

        if (lastName.length > 100) {
            Timber.w("Update profile failed: Last name too long")
            return Result.Error("Last name must be less than 100 characters")
        }

        // Validate location if provided
        if (location != null && location.isNotBlank() && location.length > 255) {
            Timber.w("Update profile failed: Location too long")
            return Result.Error("Location must be less than 255 characters")
        }

        // All validations passed, proceed with update
        Timber.d("Validation passed, proceeding with profile update")
        return userRepository.updateProfile(
            firstName = firstName.trim(),
            lastName = lastName.trim(),
            location = location?.trim(),
            maxDistance = maxDistance
        )
    }
}