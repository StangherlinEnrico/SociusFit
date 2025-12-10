package com.sociusfit.feature.profile.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO per la risposta dell'upload foto
 * Corrisponde a POST /api/profiles/me/photo
 */
data class PhotoUploadResponseDto(
    @SerializedName("photoUrl")
    val photoUrl: String
)