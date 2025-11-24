package com.sociusfit.app.data.remote.mapper

import com.sociusfit.app.data.remote.dto.auth.UserDto
import com.sociusfit.app.domain.model.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Mapper for converting between UserDto and User domain model
 */
object UserMapper {

    private val dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME

    /**
     * Convert UserDto to User domain model
     */
    fun toDomain(dto: UserDto): User {
        return User(
            id = dto.id,
            firstName = dto.firstName,
            lastName = dto.lastName,
            email = dto.email,
            isEmailVerified = dto.isEmailVerified,
            provider = dto.provider,
            location = dto.location,
            maxDistanceKm = dto.maxDistanceKm,
            createdAt = parseDateTime(dto.createdAt)
        )
    }

    /**
     * Parse ISO 8601 datetime string to LocalDateTime
     */
    private fun parseDateTime(dateTimeString: String): LocalDateTime {
        return try {
            LocalDateTime.parse(dateTimeString, dateTimeFormatter)
        } catch (e: Exception) {
            // Fallback to current time if parsing fails
            LocalDateTime.now()
        }
    }
}