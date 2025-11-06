package com.sociusfit.app.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

/**
 * DTO for registration request
 * Maps to backend CreateUserDto
 */
data class RegisterRequestDto(
    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)