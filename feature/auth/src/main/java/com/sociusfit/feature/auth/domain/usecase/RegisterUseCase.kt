package com.sociusfit.feature.auth.domain.usecase

import com.sociusfit.core.domain.Result
import com.sociusfit.core.domain.model.User
import com.sociusfit.feature.auth.domain.repository.AuthRepository

/**
 * Register Use Case
 *
 * Gestisce la logica di registrazione di un nuovo utente.
 * Valida gli input e delega al repository per l'operazione di registrazione.
 */
class RegisterUseCase(
    private val authRepository: AuthRepository
) {

    /**
     * Esegue la registrazione
     *
     * @param firstName Nome dell'utente (min 2 caratteri)
     * @param lastName Cognome dell'utente (min 2 caratteri)
     * @param email Email valida
     * @param password Password (min 8 caratteri, deve contenere almeno un numero)
     * @return Result<User> Success con User registrato o Error con messaggio
     */
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<User> {
        // Validazione firstName
        if (firstName.isBlank() || firstName.length < 2) {
            return Result.Error(
                exception = IllegalArgumentException("Il nome deve contenere almeno 2 caratteri")
            )
        }

        // Validazione lastName
        if (lastName.isBlank() || lastName.length < 2) {
            return Result.Error(
                exception = IllegalArgumentException("Il cognome deve contenere almeno 2 caratteri")
            )
        }

        // Validazione email
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!email.matches(emailRegex)) {
            return Result.Error(
                exception = IllegalArgumentException("Inserisci un'email valida")
            )
        }

        // Validazione password
        if (password.length < 8) {
            return Result.Error(
                exception = IllegalArgumentException("La password deve contenere almeno 8 caratteri")
            )
        }

        if (!password.any { it.isDigit() }) {
            return Result.Error(
                exception = IllegalArgumentException("La password deve contenere almeno un numero")
            )
        }

        // Delega al repository
        return authRepository.register(
            firstName = firstName.trim(),
            lastName = lastName.trim(),
            email = email.trim().lowercase(),
            password = password
        )
    }
}