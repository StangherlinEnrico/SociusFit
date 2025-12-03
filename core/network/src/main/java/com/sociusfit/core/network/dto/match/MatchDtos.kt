package com.sociusfit.core.network.dto.match

import com.sociusfit.core.network.dto.profile.ProfileSportDto

data class MatchDto(
    val matchId: String,
    val otherUserId: String,
    val otherUserName: String,
    val otherUserPhotoUrl: String?,
    val commonSports: List<ProfileSportDto>,
    val createdAt: String
)