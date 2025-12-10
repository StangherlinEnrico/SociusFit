package com.sociusfit.feature.profile.domain.model

/**
 * Livelli di competenza per gli sport
 */
enum class SportLevel {
    Beginner,      // Principiante
    Intermediate,  // Intermedio
    Advanced,      // Avanzato
    Expert;        // Esperto

    fun toDisplayString(): String {
        return when (this) {
            Beginner -> "Principiante"
            Intermediate -> "Intermedio"
            Advanced -> "Avanzato"
            Expert -> "Esperto"
        }
    }

    companion object {
        fun fromString(value: String): SportLevel {
            return valueOf(value)
        }
    }
}