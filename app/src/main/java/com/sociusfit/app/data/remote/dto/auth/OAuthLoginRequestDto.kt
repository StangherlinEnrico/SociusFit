package com.sociusfit.app.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

/**
 * DTO for OAuth login request
 * Maps to backend OAuthLoginDto
 */
data class OAuthLoginRequestDto(
    @SerializedName("provider")
    val provider: String,

    @SerializedName("token")
    val token: String
)