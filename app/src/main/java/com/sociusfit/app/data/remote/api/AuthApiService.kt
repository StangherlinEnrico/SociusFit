package com.sociusfit.app.data.remote.api

import com.sociusfit.app.data.remote.dto.auth.AuthResponseDto
import com.sociusfit.app.data.remote.dto.auth.LoginRequestDto
import com.sociusfit.app.data.remote.dto.auth.OAuthLoginRequestDto
import com.sociusfit.app.data.remote.dto.auth.RegisterRequestDto
import com.sociusfit.app.data.remote.dto.common.ApiResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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
     * Reset password with token
     * POST /api/v1/auth/reset-password
     */
    @POST("api/v1/auth/reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequestDto
    ): Response<ApiResult<String>>

    /**
     * Verify email with token
     * GET /api/v1/auth/verify-email
     */
    @GET("api/v1/auth/verify-email")
    suspend fun verifyEmail(
        @Query("token") token: String
    ): Response<ApiResult<String>>

    /**
     * Resend verification email
     * POST /api/v1/auth/resend-verification
     */
    @POST("api/v1/auth/resend-verification")
    suspend fun resendVerification(
        @Body request: ResendVerificationRequestDto
    ): Response<ApiResult<String>>

    /**
     * Change password for authenticated user
     * POST /api/v1/auth/change-password
     * Requires authentication
     */
    @POST("api/v1/auth/change-password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequestDto
    ): Response<ApiResult<String>>

    /**
     * Refresh access token
     * POST /api/v1/auth/refresh-token
     */
    @POST("api/v1/auth/refresh-token")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequestDto
    ): Response<ApiResult<AuthResponseDto>>

    /**
     * Revoke refresh token
     * POST /api/v1/auth/revoke-token
     * Requires authentication
     */
    @POST("api/v1/auth/revoke-token")
    suspend fun revokeToken(
        @Body request: RevokeTokenRequestDto
    ): Response<ApiResult<Unit>>

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

/**
 * Request DTO for reset password
 */
data class ResetPasswordRequestDto(
    val token: String,
    val newPassword: String
)

/**
 * Request DTO for resend verification
 */
data class ResendVerificationRequestDto(
    val email: String
)

/**
 * Request DTO for change password
 */
data class ChangePasswordRequestDto(
    val currentPassword: String,
    val newPassword: String
)

/**
 * Request DTO for refresh token
 */
data class RefreshTokenRequestDto(
    val refreshToken: String
)

/**
 * Request DTO for revoke token
 */
data class RevokeTokenRequestDto(
    val refreshToken: String
)