package com.sociusfit.app.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class Match(
    val id: String,
    val userId: String,
    val user: User,
    val sport: String,
    val compatibilityScore: Float, // 0.0 - 1.0
    val distanceKm: Double,
    val commonTimeSlots: List<TimeSlot>,
    val status: MatchStatus = MatchStatus.PENDING
)

data class MatchRequest(
    val sport: String,
    val timeSlot: TimeSlot,
    val date: LocalDate
)

enum class MatchStatus {
    PENDING,        // In attesa di risposta
    ACCEPTED,       // Entrambi hanno accettato
    REJECTED,       // Uno ha rifiutato
    EXPIRED         // Scaduto (dopo un certo tempo senza risposta)
}

enum class SwipeDirection {
    LEFT,   // Non interessato
    RIGHT   // Interessato
}

data class SwipeResult(
    val matchId: String,
    val isMatch: Boolean,  // true se entrambi hanno fatto swipe right
    val matchedUser: User? = null
)

data class MatchHistory(
    val match: Match,
    val swipedAt: LocalDateTime,
    val direction: SwipeDirection
)