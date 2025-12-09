package com.sociusfit.feature.auth.domain.usecase

/**
 * Validate Password Use Case
 *
 * Validazione real-time della password durante l'input.
 * Utilizzato per fornire feedback immediato all'utente.
 */
class ValidatePasswordUseCase {

    /**
     * Valida la password
     *
     * Requisiti:
     * - Minimo 8 caratteri
     * - Almeno un numero
     *
     * @param password Password da validare
     * @return Pair<Boolean, String?> - (isValid, errorMessage)
     */
    operator fun invoke(password: String): Pair<Boolean, String?> {
        // Password vuota è valida durante l'input
        if (password.isBlank()) {
            return true to null
        }

        // Verifica lunghezza minima
        if (password.length < 8) {
            return false to "Minimo 8 caratteri"
        }

        // Verifica presenza di almeno un numero
        if (!password.any { it.isDigit() }) {
            return false to "Deve contenere almeno un numero"
        }

        return true to null
    }

    /**
     * Ottiene i criteri di validazione
     *
     * @return Lista di requisiti password
     */
    fun getPasswordRequirements(): List<String> {
        return listOf(
            "Minimo 8 caratteri",
            "Almeno un numero"
        )
    }
}