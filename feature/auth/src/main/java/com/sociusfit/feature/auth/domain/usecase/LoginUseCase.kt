package com.sociusfit.feature.auth.domain.usecase

import com.sociusfit.core.domain.Result
import com.sociusfit.core.domain.model.User
import com.sociusfit.feature.auth.domain.repository.AuthRepository

/**
 * Login Use Case
 *
 * Gestisce la logica di login di un utente esistente.
 * Valida gli input e delega al repository per l'operazione di login.
 */
class LoginUseCase(
    private val authRepository: AuthRepository
) {

    /**
     * Esegue il login
     *
     * @param email Email dell'utente
     * @param password Password dell'utente
     * @return Result<User> Success con User autenticato o Error con messaggio
     */
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<User> {
        // Validazione email
        if (email.isBlank()) {
            return Result.Error(
                exception = IllegalArgumentException("Inserisci la tua email")
            )
        }

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!email.matches(emailRegex)) {
            return Result.Error(
                exception = IllegalArgumentException("Inserisci un'email valida")
            )
        }

        // Validazione password
        if (password.isBlank()) {
            return Result.Error(
                exception = IllegalArgumentException("Inserisci la tua password")
            )
        }

        // Delega al repository
        return authRepository.login(
            email = email.trim().lowercase(),
            password = password
        )
    }
}