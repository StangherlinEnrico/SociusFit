package com.sociusfit.app.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

/**
 * DTO for user data
 * Maps to backend UserDto
 */
data class UserDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("isEmailVerified")
    val isEmailVerified: Boolean,

    @SerializedName("provider")
    val provider: String? = null,

    @SerializedName("location")
    val location: String? = null,

    @SerializedName("maxDistance")
    val maxDistance: Int? = null,

    @SerializedName("createdAt")
    val createdAt: String // ISO 8601 format
)