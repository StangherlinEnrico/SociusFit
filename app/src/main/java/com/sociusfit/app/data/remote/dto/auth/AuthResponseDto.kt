package com.sociusfit.app.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

/**
 * DTO for authentication response
 * Maps to backend AuthResponseDto
 */
data class AuthResponseDto(
    @SerializedName("token")
    val token: String,

    @SerializedName("expiresAt")
    val expiresAt: String, // ISO 8601 format

    @SerializedName("user")
    val user: UserDto
)