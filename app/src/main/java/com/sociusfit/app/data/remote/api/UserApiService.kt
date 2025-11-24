package com.sociusfit.app.data.remote.api

import com.sociusfit.app.data.remote.dto.auth.UserDto
import com.sociusfit.app.data.remote.dto.common.ApiResult
import retrofit2.Response
import retrofit2.http.*

/**
 * Retrofit API service for user endpoints
 * Base path: /api/v1/users
 * Requires authentication token in header
 */
interface UserApiService {

    /**
     * Get current authenticated user profile
     * GET /api/v1/users/me
     */
    @GET("api/v1/users/me")
    suspend fun getCurrentUser(): Response<ApiResult<UserDto>>

    /**
     * Get user by ID
     * GET /api/v1/users/{id}
     */
    @GET("api/v1/users/{id}")
    suspend fun getUserById(
        @Path("id") userId: Int
    ): Response<ApiResult<UserDto>>

    /**
     * Update current user profile
     * PUT /api/v1/users/me/profile
     */
    @PUT("api/v1/users/me/profile")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequest
    ): Response<ApiResult<UserDto>>

    /**
     * Update current user location
     * PUT /api/v1/users/me/location
     */
    @PUT("api/v1/users/me/location")
    suspend fun updateLocation(
        @Body request: UpdateLocationRequest
    ): Response<ApiResult<UserDto>>

    /**
     * Delete current user account
     * DELETE /api/v1/users/me
     */
    @DELETE("api/v1/users/me")
    suspend fun deleteAccount(): Response<ApiResult<Unit>>
}

/**
 * Request DTO for updating user profile
 */
data class UpdateProfileRequest(
    val firstName: String,
    val lastName: String,
    val location: String? = null
)

/**
 * Request DTO for updating user location
 */
data class UpdateLocationRequest(
    val latitude: Double,
    val longitude: Double,
    val maxDistanceKm: Int
)