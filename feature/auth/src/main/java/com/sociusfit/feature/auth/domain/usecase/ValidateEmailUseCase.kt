package com.sociusfit.feature.auth.domain.usecase

/**
 * Validate Email Use Case
 *
 * Validazione real-time dell'email durante l'input.
 * Utilizzato per fornire feedback immediato all'utente.
 */
class ValidateEmailUseCase {

    /**
     * Valida il formato dell'email
     *
     * @param email Email da validare
     * @return Pair<Boolean, String?> - (isValid, errorMessage)
     */
    operator fun invoke(email: String): Pair<Boolean, String?> {
        // Email vuota è valida durante l'input (validazione finale nel use case register/login)
        if (email.isBlank()) {
            return true to null
        }

        // Regex per validazione email (RFC 5322 semplificato)
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

        return if (email.matches(emailRegex)) {
            true to null
        } else {
            false to "Email non valida"
        }
    }
}