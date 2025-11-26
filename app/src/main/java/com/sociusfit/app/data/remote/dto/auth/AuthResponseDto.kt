package com.sociusfit.app.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

/**
 * DTO for authentication response
 * Maps to backend AuthResponseDto
 */
data class AuthResponseDto(
    @SerializedName("token")
    val token: String,

    @SerializedName("refreshToken")
    val refreshToken: String,

    @SerializedName("expiresAt")
    val expiresAt: String,

    @SerializedName("user")
    val user: UserDto
)