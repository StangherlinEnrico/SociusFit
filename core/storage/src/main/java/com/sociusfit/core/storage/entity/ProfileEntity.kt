package com.sociusfit.core.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val bio: String?,
    val city: String?,
    val latitude: Double?,
    val longitude: Double?,
    val maxDistance: Int,
    val photoUrl: String?
)