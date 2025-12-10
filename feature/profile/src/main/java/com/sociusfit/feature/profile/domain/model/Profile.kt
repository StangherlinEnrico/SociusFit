package com.sociusfit.feature.profile.domain.model

/**
 * Domain model per il profilo utente
 */
data class Profile(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val bio: String,
    val maxDistance: Int, // in km
    val photoUrl: String?,
    val sports: List<ProfileSport>
) {
    val fullName: String
        get() = "$firstName $lastName"

    companion object {
        const val MIN_AGE = 18
        const val MAX_AGE = 100
        const val MIN_DISTANCE = 5
        const val MAX_DISTANCE = 100
        const val MAX_BIO_LENGTH = 500
        const val MIN_SPORTS = 1
        const val MAX_SPORTS = 5
    }
}