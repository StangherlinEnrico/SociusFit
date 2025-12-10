package com.sociusfit.feature.profile.domain.model

/**
 * Sport selezionato dall'utente con livello di competenza
 */
data class ProfileSport(
    val sportId: String,
    val sportName: String,
    val level: SportLevel
)