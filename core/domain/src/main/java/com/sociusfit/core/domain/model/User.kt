package com.sociusfit.core.domain.model

/**
 * User Domain Model
 *
 * Rappresenta l'utente autenticato nell'applicazione.
 * Questo è il modello del domain layer, indipendente da framework Android.
 *
 * NOTA Sprint 1:
 * - profilePhotoUrl e profileComplete saranno popolati in Sprint 2
 * - Per ora sono opzionali con valori di default
 */
data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val profilePhotoUrl: String? = null,
    val profileComplete: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    /**
     * Nome completo dell'utente
     */
    val fullName: String
        get() = "$firstName $lastName"

    companion object {
        /**
         * Utente vuoto per stati iniziali
         */
        val EMPTY = User(
            id = "",
            firstName = "",
            lastName = "",
            email = "",
            profileComplete = false
        )
    }
}