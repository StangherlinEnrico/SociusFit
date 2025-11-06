package com.sociusfit.app.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

/**
 * DTO for login request
 * Maps to backend LoginDto
 */
data class LoginRequestDto(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)