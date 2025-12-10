package com.sociusfit.feature.profile.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO per gli sport del profilo con livello
 */
data class ProfileSportDto(
    @SerializedName("sportId")
    val sportId: String,

    @SerializedName("sportName")
    val sportName: String,

    @SerializedName("level")
    val level: String // "Beginner", "Intermediate", "Advanced", "Expert"
)