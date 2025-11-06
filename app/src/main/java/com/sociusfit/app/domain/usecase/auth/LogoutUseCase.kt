package com.sociusfit.app.domain.usecase.auth

import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.repository.AuthRepository
import timber.log.Timber

/**
 * Use case for user logout
 * Handles session cleanup and data clearing
 */
class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Execute logout
     * Clears all local session data and authentication token
     * @return Result indicating success or error
     */
    suspend operator fun invoke(): Result<Unit> {
        Timber.d("Executing logout")
        return authRepository.logout()
    }
}