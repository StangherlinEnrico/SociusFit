package com.sociusfit.app.domain.model

import java.time.LocalDateTime

/**
 * Domain model representing authentication response
 */
data class AuthResponse(
    val token: String,
    val expiresAt: LocalDateTime,
    val user: User
) {
    /**
     * Check if token is expired
     */
    fun isExpired(): Boolean = LocalDateTime.now().isAfter(expiresAt)

    /**
     * Check if token is still valid
     */
    fun isValid(): Boolean = !isExpired()
}