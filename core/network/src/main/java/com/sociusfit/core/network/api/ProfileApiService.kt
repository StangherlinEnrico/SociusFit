// core/network/src/main/java/com/sociusfit/core/network/api/ProfileApiService.kt

package com.sociusfit.core.network.api

import com.sociusfit.core.network.dto.profile.CreateProfileRequest
import com.sociusfit.core.network.dto.profile.PhotoUploadResponse
import com.sociusfit.core.network.dto.profile.ProfileDto
import com.sociusfit.core.network.dto.profile.UpdateProfileRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileApiService {
    /**
     * Get current user's profile
     * Backend: GET /api/profiles/me
     */
    @GET("profiles/me")
    suspend fun getMyProfile(): ProfileDto

    /**
     * Create profile for current user (prima volta, onboarding)
     * Backend: POST /api/profiles
     * IMPORTANTE: Usare questo per CREARE il profilo la prima volta!
     */
    @POST("profiles")
    suspend fun createProfile(@Body request: CreateProfileRequest): ProfileDto

    /**
     * Update existing profile for current user
     * Backend: PUT /api/profiles (NO /me!)
     * IMPORTANTE: Endpoint corretto è /profiles, NON /profiles/me!
     */
    @PUT("profiles")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): ProfileDto

    /**
     * Upload profile photo
     * Backend: POST /api/profiles/photo (NO /me!)
     * User identificato da JWT token nell'Authorization header
     */
    @Multipart
    @POST("profiles/photo")
    suspend fun uploadPhoto(@Part photo: MultipartBody.Part): PhotoUploadResponse

    /**
     * Get profile by user ID
     * Backend: GET /api/profiles/user/{userId}
     */
    @GET("profiles/user/{userId}")
    suspend fun getProfile(@Path("userId") userId: String): ProfileDto
}