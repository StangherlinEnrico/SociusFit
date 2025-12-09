package com.sociusfit.feature.auth.domain.usecase

import com.sociusfit.core.domain.Result
import com.sociusfit.feature.auth.domain.repository.AuthRepository

/**
 * Logout Use Case
 *
 * Gestisce la logica di logout dell'utente.
 * Invalida il token sul backend e rimuove tutti i dati locali.
 */
class LogoutUseCase(
    private val authRepository: AuthRepository
) {

    /**
     * Esegue il logout
     *
     * @return Result<Unit> Success se logout completato, Error in caso di errore
     */
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout()
    }
}