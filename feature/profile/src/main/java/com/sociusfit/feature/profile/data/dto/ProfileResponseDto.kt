package com.sociusfit.feature.profile.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO per la risposta del profilo
 * Corrisponde a GET /api/profiles/me e GET /api/profiles/{userId}
 */
data class ProfileResponseDto(
    @SerializedName("userId")
    val userId: String,

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("age")
    val age: Int,

    @SerializedName("city")
    val city: String,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("bio")
    val bio: String,

    @SerializedName("maxDistance")
    val maxDistance: Int,

    @SerializedName("photoUrl")
    val photoUrl: String?,

    @SerializedName("sports")
    val sports: List<ProfileSportDto>,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String
)