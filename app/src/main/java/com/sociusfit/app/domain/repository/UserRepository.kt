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
     * Update user profile (ONLY firstName and lastName)
     * @param firstName User first name
     * @param lastName User last name
     * @return Result with updated User or error
     */
    suspend fun updateProfile(
        firstName: String,
        lastName: String
    ): Result<User>

    /**
     * 🔥 FIXED: Update user location settings
     * @param locationCode Municipality ISTAT code (e.g., "026086" for Treviso)
     * @param maxDistance Maximum distance for matches in kilometers (nullable)
     * @return Result with updated User or error
     */
    suspend fun updateLocation(
        locationCode: String?,
        maxDistance: Int?
    ): Result<User>

    /**
     * Delete user account
     * @return Result indicating success or error
     */
    suspend fun deleteAccount(): Result<Unit>
}