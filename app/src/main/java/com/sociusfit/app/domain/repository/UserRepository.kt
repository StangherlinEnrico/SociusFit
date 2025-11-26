package com.sociusfit.app.domain.repository

import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.model.User

/**
 * Repository interface for user operations
 */
interface UserRepository {

    /**
     * Get user by ID
     * @param userId User ID
     * @return Result with User or error
     */
    suspend fun getUserById(userId: Int): Result<User>

    /**
     * Get current logged in user
     * @return Result with User or error
     */
    suspend fun getCurrentUser(): Result<User>

    /**
     * Update user profile
     * @param firstName User first name
     * @param lastName User last name
     * @param location User location description
     * @return Result with updated User or error
     */
    suspend fun updateProfile(
        firstName: String,
        lastName: String,
        location: String?,
        maxDistance: Int
    ): Result<User>

    /**
     * Update user location coordinates
     * @param latitude Latitude coordinate
     * @param longitude Longitude coordinate
     * @param maxDistance Maximum distance for matches in kilometers
     * @return Result with updated User or error
     */
    suspend fun updateLocation(
        latitude: Double,
        longitude: Double,
        maxDistance: Int
    ): Result<User>

    /**
     * Delete user account
     * @return Result indicating success or error
     */
    suspend fun deleteAccount(): Result<Unit>
}