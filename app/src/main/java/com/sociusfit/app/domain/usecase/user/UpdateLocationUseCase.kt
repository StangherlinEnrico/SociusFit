package com.sociusfit.app.domain.usecase.user

import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.model.User
import com.sociusfit.app.domain.repository.UserRepository
import timber.log.Timber

/**
 * 🔥 FIXED: Use case for updating user location settings
 * Handles validation and business logic for location updates
 */
class UpdateLocationUseCase(
    private val userRepository: UserRepository
) {
    /**
     * Execute location update
     * @param locationCode Municipality ISTAT code (e.g., "026086" for Treviso)
     * @param maxDistance Maximum distance for matching in kilometers (1-500)
     * @return Result with updated User or error
     */
    suspend operator fun invoke(
        locationCode: String?,
        maxDistance: Int?
    ): Result<User> {
        // Validate location code if provided
        if (locationCode != null && locationCode.isNotBlank()) {
            // ISTAT codes are 6 digits
            if (!locationCode.matches(Regex("^\\d{6}$"))) {
                Timber.w("Update location failed: Invalid location code format: $locationCode")
                return Result.Error("Invalid location code format")
            }
        }

        // Validate max distance if provided
        if (maxDistance != null) {
            if (maxDistance < 1) {
                Timber.w("Update location failed: Max distance too small: $maxDistance")
                return Result.Error("Maximum distance must be at least 1 km")
            }

            if (maxDistance > 500) {
                Timber.w("Update location failed: Max distance too large: $maxDistance")
                return Result.Error("Maximum distance cannot exceed 500 km")
            }
        }

        // All validations passed, proceed with update
        Timber.d("Validation passed, proceeding with location update")
        return userRepository.updateLocation(
            locationCode = locationCode?.trim(),
            maxDistance = maxDistance
        )
    }
}