package com.sociusfit.core.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey val id: String,
    val otherUserId: String,
    val otherUserName: String,
    val otherUserPhotoUrl: String?,
    val createdAt: Long,
    val seen: Boolean = false
)