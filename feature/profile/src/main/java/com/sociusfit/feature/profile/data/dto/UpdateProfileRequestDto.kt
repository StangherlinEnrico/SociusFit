package com.sociusfit.feature.profile.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO per aggiornare il profilo esistente
 * Corrisponde a PUT /api/profiles/me
 */
data class UpdateProfileRequestDto(
    @SerializedName("age")
    val age: Int?,

    @SerializedName("city")
    val city: String?,

    @SerializedName("latitude")
    val latitude: Double?,

    @SerializedName("longitude")
    val longitude: Double?,

    @SerializedName("bio")
    val bio: String?,

    @SerializedName("maxDistance")
    val maxDistance: Int?,

    @SerializedName("sports")
    val sports: List<ProfileSportDto>?
)