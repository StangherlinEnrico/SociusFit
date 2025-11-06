package com.sociusfit.app.domain.model

import java.time.LocalDateTime

/**
 * Domain model representing a user in the application
 */
data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val isEmailVerified: Boolean,
    val provider: String? = null,
    val location: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val maxDistanceKm: Int? = null,
    val createdAt: LocalDateTime
) {
    val fullName: String
        get() = "$firstName $lastName"

    /**
     * Check if user logged in with OAuth provider
     */
    fun isOAuthUser(): Boolean = provider != null

    /**
     * Check if user has set location preferences
     */
    fun hasLocationSet(): Boolean = latitude != null && longitude != null
}