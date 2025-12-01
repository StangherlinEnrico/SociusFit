package com.sociusfit.feature.user.data.remote.api

import com.sociusfit.feature.user.data.remote.dto.ApiResult
import com.sociusfit.feature.user.data.remote.dto.UpdateLocationRequestDto
import com.sociusfit.feature.user.data.remote.dto.UpdateProfileRequestDto
import com.sociusfit.feature.user.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PUT

interface UserApiService {

    @PUT("api/v1/users/me/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequestDto): Response<ApiResult<UserDto>>

    @PUT("api/v1/users/me/location")
    suspend fun updateLocation(@Body request: UpdateLocationRequestDto): Response<ApiResult<UserDto>>

    @DELETE("api/v1/users/me")
    suspend fun deleteAccount(): Response<ApiResult<Unit>>
}