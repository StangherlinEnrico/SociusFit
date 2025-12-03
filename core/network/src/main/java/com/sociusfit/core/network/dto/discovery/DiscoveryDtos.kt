package com.sociusfit.core.network.dto.discovery

import com.sociusfit.core.network.dto.profile.ProfileSportDto

data class ProfileCardDto(
    val userId: String,
    val firstName: String,
    val age: Int,
    val city: String,
    val distance: Double,
    val bio: String?,
    val photoUrl: String?,
    val sports: List<ProfileSportDto>,
    val compatibility: Double
)

data class SwipeLikeRequest(
    val likedUserId: String
)

data class SwipeLikeResponse(
    val isMatch: Boolean,
    val matchId: String?,
    val matchedUserName: String?
)