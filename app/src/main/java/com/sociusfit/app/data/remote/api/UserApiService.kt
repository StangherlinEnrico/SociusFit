package com.sociusfit.app.data.remote.api

import com.sociusfit.app.data.remote.dto.auth.UserDto
import com.sociusfit.app.data.remote.dto.common.ApiResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Retrofit API service for user endpoints
 * Base path: /api/v1/users
 * Requires authentication token in header
 */
interface UserApiService {

    /**
     * Get user by ID
     * GET /api/v1/users/{id}
     */
    @GET("api/v1/users/{id}")
    suspend fun getUserById(
        @Path("id") userId: Int
    ): Response<ApiResult<UserDto>>

    /**
     * Update user profile
     * PUT /api/v1/users/profile
     */
    @PUT("api/v1/users/profile")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequest
    ): Response<ApiResult<UserDto>>

    /**
     * Update user location
     * PUT /api/v1/users/location
     */
    @PUT("api/v1/users/location")
    suspend fun updateLocation(
        @Body request: UpdateLocationRequest
    ): Response<ApiResult<UserDto>>

    /**
     * Delete user account
     * DELETE /api/v1/users
     */
    @DELETE("api/v1/users")
    suspend fun deleteAccount(): Response<ApiResult<Unit>>
}

/**
 * Request DTO for updating user profile
 */
data class UpdateProfileRequest(
    val firstName: String,
    val lastName: String,
    val location: String?
)

/**
 * Request DTO for updating user location
 */
data class UpdateLocationRequest(
    val latitude: Double,
    val longitude: Double,
    val maxDistanceKm: Int
)