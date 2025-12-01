package com.sociusfit.feature.user.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequestDto(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginRequestDto(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class UpdateProfileRequestDto(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String
)

data class UpdateLocationRequestDto(
    @SerializedName("location") val location: String?,
    @SerializedName("maxDistance") val maxDistance: Int?
)