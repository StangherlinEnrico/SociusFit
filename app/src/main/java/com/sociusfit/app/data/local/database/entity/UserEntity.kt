package com.sociusfit.app.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sociusfit.app.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val city: String,
    val radiusKm: Int,
    val profileImageUrl: String?,
    val sportsJson: String,
    val availabilityJson: String
)

fun UserEntity.toDomain(): User {
    // TODO: Implementare deserializzazione JSON per sports e availability
    return User(
        id = id,
        name = name,
        email = email,
        city = city,
        radiusKm = radiusKm,
        sports = emptyList(), // TODO: Parse from sportsJson
        availability = emptyList(), // TODO: Parse from availabilityJson
        profileImageUrl = profileImageUrl
    )
}

fun User.toEntity(): UserEntity {
    // TODO: Implementare serializzazione JSON per sports e availability
    return UserEntity(
        id = id,
        name = name,
        email = email,
        city = city,
        radiusKm = radiusKm,
        profileImageUrl = profileImageUrl,
        sportsJson = "", // TODO: Serialize sports to JSON
        availabilityJson = "" // TODO: Serialize availability to JSON
    )
}