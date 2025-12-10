package com.sociusfit.feature.profile.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO per gli sport disponibili
 * Corrisponde a GET /api/sports
 */
data class SportDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("createdAt")
    val createdAt: String
)

/**
 * Response wrapper per lista sport
 */
data class SportsResponseDto(
    @SerializedName("sports")
    val sports: List<SportDto>
)