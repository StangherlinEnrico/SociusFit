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
 * Base path: /api/v1/users
 */
interface AuthApiService {

    /**
     * Register a new user
     * POST /api/v1/users/register
     */
    @POST("api/v1/users/register")
    suspend fun register(
        @Body request: RegisterRequestDto
    ): Response<ApiResult<AuthResponseDto>>

    /**
     * Login with email and password
     * POST /api/v1/users/login
     */
    @POST("api/v1/users/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<ApiResult<AuthResponseDto>>

    /**
     * Login with OAuth provider (Google, Facebook, Microsoft, Apple)
     * POST /api/v1/users/login/oauth
     */
    @POST("api/v1/users/login/oauth")
    suspend fun loginWithOAuth(
        @Body request: OAuthLoginRequestDto
    ): Response<ApiResult<AuthResponseDto>>
}