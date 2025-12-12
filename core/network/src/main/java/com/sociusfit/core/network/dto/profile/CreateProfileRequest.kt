package com.sociusfit.core.network.dto.profile

import com.google.gson.annotations.SerializedName

/**
 * Request DTO per creazione profilo (POST /api/profiles)
 * Corrisponde al backend CreateProfileRequest
 */
data class CreateProfileRequest(
    @SerializedName("age")
    val age: Int,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("city")
    val city: String, // Formato: "Comune, Regione" (es: "Cortemilia, Piemonte")

    @SerializedName("bio")
    val bio: String,

    @SerializedName("maxDistance")
    val maxDistance: Int,

    @SerializedName("sports")
    val sports: List<SportLevelRequest>
)