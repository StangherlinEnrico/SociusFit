package com.sociusfit.app.data.remote.api

import com.sociusfit.app.data.remote.dto.auth.AuthResponseDto
import com.sociusfit.app.data.remote.dto.auth.LoginRequestDto
import com.sociusfit.app.data.remote.dto.auth.OAuthLoginRequestDto
import com.sociusfit.app.data.remote.dto.auth.RegisterRequestDto
import com.sociusfit.app.data.remote.dto.common.ApiResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit API service for authentication endpoints
 * Base path: /api/v1/auth
 */
interface AuthApiService {

    /**
     * Register a new user
     * POST /api/v1/auth/register
     */
    @POST("api/v1/auth/register")
    suspend fun register(
        @Body request: RegisterRequestDto
    ): Response<ApiResult<AuthResponseDto>>

    /**
     * Login with email and password
     * POST /api/v1/auth/login
     */
    @POST("api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<ApiResult<AuthResponseDto>>

    /**
     * Login with OAuth provider (Google, Apple)
     * POST /api/v1/auth/login/oauth
     */
    @POST("api/v1/auth/login/oauth")
    suspend fun loginWithOAuth(
        @Body request: OAuthLoginRequestDto
    ): Response<ApiResult<AuthResponseDto>>

    /**
     * Request password reset
     * POST /api/v1/auth/forgot-password
     */
    @POST("api/v1/auth/forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequestDto
    ): Response<ApiResult<String>>

    /**
     * Logout user (revoke all refresh tokens)
     * POST /api/v1/auth/logout
     * Requires authentication
     */
    @POST("api/v1/auth/logout")
    suspend fun logout(): Response<ApiResult<Unit>>
}

/**
 * Request DTO for forgot password
 */
data class ForgotPasswordRequestDto(
    val email: String
)