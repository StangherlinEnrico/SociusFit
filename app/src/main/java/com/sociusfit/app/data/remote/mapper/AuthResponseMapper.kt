package com.sociusfit.app.data.remote.mapper

import com.sociusfit.app.data.remote.dto.auth.AuthResponseDto
import com.sociusfit.app.domain.model.AuthResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Mapper for converting between AuthResponseDto and AuthResponse domain model
 */
object AuthResponseMapper {

    private val dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME

    /**
     * Convert AuthResponseDto to AuthResponse domain model
     */
    fun toDomain(dto: AuthResponseDto): AuthResponse {
        return AuthResponse(
            token = dto.token,
            expiresAt = parseDateTime(dto.expiresAt),
            user = UserMapper.toDomain(dto.user)
        )
    }

    /**
     * Parse ISO 8601 datetime string to LocalDateTime
     */
    private fun parseDateTime(dateTimeString: String): LocalDateTime {
        return try {
            LocalDateTime.parse(dateTimeString, dateTimeFormatter)
        } catch (e: Exception) {
            // Fallback to 7 days from now if parsing fails
            LocalDateTime.now().plusDays(7)
        }
    }
}