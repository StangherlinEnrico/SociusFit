package com.sociusfit.core.storage.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "sports")
data class SportEntity(
    @PrimaryKey val id: String,
    val name: String,
    val iconUrl: String?
)

@Entity(
    tableName = "profile_sports",
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SportEntity::class,
            parentColumns = ["id"],
            childColumns = ["sportId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("profileId"), Index("sportId")]
)
data class ProfileSportEntity(
    @PrimaryKey val id: String,
    val profileId: String,
    val sportId: String,
    val level: String
)