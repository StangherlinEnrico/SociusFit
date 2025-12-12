package com.sociusfit.core.network.dto.profile

import com.google.gson.annotations.SerializedName

/**
 * Request DTO per aggiornare profilo esistente (PUT /api/profiles)
 * Corrisponde al backend UpdateProfileRequest
 * IMPORTANTE: Backend richiede age e gender anche nell'update!
 */
data class UpdateProfileRequest(
    @SerializedName("age")
    val age: Int,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("bio")
    val bio: String,

    @SerializedName("city")
    val city: String, // Formato: "Comune, Regione"

    @SerializedName("maxDistance")
    val maxDistance: Int,

    @SerializedName("sports")
    val sports: List<SportLevelRequest>
)