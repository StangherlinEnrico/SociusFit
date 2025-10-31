package com.sociusfit.app.data.remote.api

import com.sociusfit.app.data.remote.dto.LoginRequest
import com.sociusfit.app.data.remote.dto.LoginResponse
import com.sociusfit.app.data.remote.dto.RefreshTokenRequest
import com.sociusfit.app.data.remote.dto.RefreshTokenResponse
import com.sociusfit.app.data.remote.dto.RegisterRequest
import com.sociusfit.app.data.remote.dto.RegisterResponse
import com.sociusfit.app.data.remote.dto.ValidateTokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): RefreshTokenResponse

    @GET("auth/validate")
    suspend fun validateToken(): ValidateTokenResponse
}