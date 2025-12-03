package com.sociusfit.core.network.api

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
    @GET("profiles/me")
    suspend fun getMyProfile(): ProfileDto

    @PUT("profiles/me")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): ProfileDto

    @Multipart
    @POST("profiles/me/photo")
    suspend fun uploadPhoto(@Part photo: MultipartBody.Part): PhotoUploadResponse

    @GET("profiles/{userId}")
    suspend fun getProfile(@Path("userId") userId: String): ProfileDto
}