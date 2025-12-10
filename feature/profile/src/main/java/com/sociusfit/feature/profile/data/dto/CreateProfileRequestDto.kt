package com.sociusfit.feature.profile.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO per creare un nuovo profilo
 * Corrisponde a POST /api/profiles
 */
data class CreateProfileRequestDto(
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

    @SerializedName("sports")
    val sports: List<ProfileSportDto>
)