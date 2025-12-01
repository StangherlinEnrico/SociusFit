package com.sociusfit.feature.user.data.remote.api

import com.sociusfit.feature.user.data.remote.dto.AuthResponseDto
import com.sociusfit.feature.user.data.remote.dto.LoginRequestDto
import com.sociusfit.feature.user.data.remote.dto.RegisterRequestDto
import com.sociusfit.feature.user.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {

    /**
     * Register new user
     * POST /api/users/register
     */
    @POST("api/users/register")
    suspend fun register(
        @Body request: RegisterRequestDto
    ): Response<AuthResponseDto>

    /**
     * Login user
     * POST /api/users/login
     */
    @POST("api/users/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<AuthResponseDto>

    /**
     * Get current authenticated user
     * GET /api/users/me
     */
    @GET("api/users/me")
    suspend fun getCurrentUser(): Response<UserDto>
}