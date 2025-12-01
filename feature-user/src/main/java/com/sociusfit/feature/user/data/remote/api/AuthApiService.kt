package com.sociusfit.feature.user.data.remote.api

import com.sociusfit.feature.user.data.remote.dto.ApiResult
import com.sociusfit.feature.user.data.remote.dto.AuthResponseDto
import com.sociusfit.feature.user.data.remote.dto.LoginRequestDto
import com.sociusfit.feature.user.data.remote.dto.RegisterRequestDto
import com.sociusfit.feature.user.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/users/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<ApiResult<AuthResponseDto>>

    @POST("api/users/login")
    suspend fun login(@Body request: LoginRequestDto): Response<ApiResult<AuthResponseDto>>

    @GET("api/users/me")
    suspend fun getCurrentUser(): Response<ApiResult<UserDto>>
}