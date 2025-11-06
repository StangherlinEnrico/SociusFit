package com.sociusfit.app.domain.usecase.auth

import com.sociusfit.app.domain.repository.AuthRepository
import timber.log.Timber

/**
 * Use case for checking if user is logged in
 * Useful for splash screen and navigation decisions
 */
class CheckLoginStatusUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Check if user is currently logged in
     * @return true if user has valid session, false otherwise
     */
    suspend operator fun invoke(): Boolean {
        val isLoggedIn = authRepository.isLoggedIn()
        Timber.d("User login status: $isLoggedIn")
        return isLoggedIn
    }
}