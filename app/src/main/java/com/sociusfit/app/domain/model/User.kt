package com.sociusfit.app.domain.model

import java.time.DayOfWeek

/**
 * Modello di dominio per l'utente
 */
data class User(
    val id: String,
    val name: String,
    val email: String,
    val city: String,
    val radiusKm: Int,
    val sports: List<UserSport>,
    val availability: List<Availability>,
    val profileImageUrl: String? = null
)

/**
 * Sport praticato dall'utente con relativo livello
 */
data class UserSport(
    val sportId: String,
    val sportName: String,
    val level: SportLevel
)

/**
 * Disponibilità dell'utente per giorno e fascia oraria
 */
data class Availability(
    val dayOfWeek: DayOfWeek,
    val timeSlot: TimeSlot
)

/**
 * Sport disponibile nella piattaforma
 */
data class Sport(
    val id: String,
    val name: String,
    val iconUrl: String? = null,
    val category: SportCategory = SportCategory.OTHER
)

/**
 * Categorie di sport
 */
enum class SportCategory {
    TEAM_SPORT,      // Calcio, Basket, Pallavolo
    RACKET_SPORT,    // Tennis, Padel, Badminton
    INDIVIDUAL,      // Running, Ciclismo, Nuoto
    FITNESS,         // Palestra, CrossFit, Yoga
    OUTDOOR,         // Arrampicata, Trekking, Mountain Bike
    WATER_SPORT,     // Surf, Kayak, Vela
    COMBAT_SPORT,    // Boxe, Arti marziali
    OTHER
}