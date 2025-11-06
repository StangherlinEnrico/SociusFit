package com.sociusfit.app.domain.usecase.user

import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.model.User
import com.sociusfit.app.domain.repository.UserRepository
import timber.log.Timber

/**
 * Use case for updating user location
 * Handles validation and business logic for location updates
 */
class UpdateLocationUseCase(
    private val userRepository: UserRepository
) {
    /**
     * Execute location update
     * @param latitude Latitude coordinate (-90 to 90)
     * @param longitude Longitude coordinate (-180 to 180)
     * @param maxDistanceKm Maximum distance for matching in kilometers (1-500)
     * @return Result with updated User or error
     */
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
        maxDistanceKm: Int
    ): Result<User> {
        // Validate latitude
        if (latitude < -90.0 || latitude > 90.0) {
            Timber.w("Update location failed: Invalid latitude: $latitude")
            return Result.Error("Latitude must be between -90 and 90")
        }

        // Validate longitude
        if (longitude < -180.0 || longitude > 180.0) {
            Timber.w("Update location failed: Invalid longitude: $longitude")
            return Result.Error("Longitude must be between -180 and 180")
        }

        // Validate max distance
        if (maxDistanceKm < 1) {
            Timber.w("Update location failed: Max distance too small: $maxDistanceKm")
            return Result.Error("Maximum distance must be at least 1 km")
        }

        if (maxDistanceKm > 500) {
            Timber.w("Update location failed: Max distance too large: $maxDistanceKm")
            return Result.Error("Maximum distance cannot exceed 500 km")
        }

        // All validations passed, proceed with update
        Timber.d("Validation passed, proceeding with location update")
        return userRepository.updateLocation(
            latitude = latitude,
            longitude = longitude,
            maxDistanceKm = maxDistanceKm
        )
    }
}