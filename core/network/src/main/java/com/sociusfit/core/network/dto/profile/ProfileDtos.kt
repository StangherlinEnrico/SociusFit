package com.sociusfit.core.network.dto.profile

data class ProfileDto(
    val id: String,
    val userId: String,
    val bio: String?,
    val city: String?,
    val latitude: Double?,
    val longitude: Double?,
    val maxDistance: Int,
    val photoUrl: String?,
    val sports: List<ProfileSportDto>,
    val gender: String
)

data class ProfileSportDto(
    val sportId: String,
    val sportName: String,
    val level: String
)

data class PhotoUploadResponse(
    val photoUrl: String
)