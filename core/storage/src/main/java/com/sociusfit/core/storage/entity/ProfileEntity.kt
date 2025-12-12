package com.sociusfit.core.storage.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity Room per il profilo utente
 */
@Entity(
    tableName = "profiles",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class ProfileEntity(
    @PrimaryKey
    val userId: String,
    val age: Int,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val bio: String,
    val maxDistance: Int,
    val photoUrl: String?,
    val updatedAt: Long,
    val gender: String
)

/**
 * Entity Room per la relazione profilo-sport
 * (Many-to-Many tra Profile e Sport)
 */
@Entity(
    tableName = "profile_sports",
    primaryKeys = ["userId", "sportId"],
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SportEntity::class,
            parentColumns = ["id"],
            childColumns = ["sportId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId"), Index("sportId")]
)
data class ProfileSportEntity(
    val userId: String,
    val sportId: String,
    val level: String // "Beginner", "Intermediate", "Advanced", "Expert"
)