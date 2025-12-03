package com.sociusfit.core.network.api

import com.sociusfit.core.network.dto.auth.AuthResponse
import com.sociusfit.core.network.dto.auth.LoginRequest
import com.sociusfit.core.network.dto.auth.RegisterRequest
import com.sociusfit.core.network.dto.auth.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/logout")
    suspend fun logout()

    @GET("users/me")
    suspend fun getCurrentUser(): UserDto
}