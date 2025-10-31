package com.sociusfit.app.domain.model

enum class SportLevel(val displayName: String) {
    BEGINNER("Principiante"),
    INTERMEDIATE("Intermedio"),
    ADVANCED("Avanzato"),
    PRO("Professionista");

    companion object {
        fun fromString(value: String): SportLevel? {
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }
    }
}

enum class TimeSlot(val displayName: String, val startHour: Int, val endHour: Int) {
    MORNING("Mattina", 6, 12),
    AFTERNOON("Pomeriggio", 12, 18),
    EVENING("Sera", 18, 22),
    NIGHT("Notte", 22, 6);

    companion object {
        fun fromString(value: String): TimeSlot? {
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }

        fun fromHour(hour: Int): TimeSlot {
            return when (hour) {
                in 6..11 -> MORNING
                in 12..17 -> AFTERNOON
                in 18..21 -> EVENING
                else -> NIGHT
            }
        }
    }
}