package com.sociusfit.app.domain.usecase.user

import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.repository.UserRepository
import timber.log.Timber

/**
 * Use case for deleting user account
 * Handles account deletion and cleanup
 */
class DeleteAccountUseCase(
    private val userRepository: UserRepository
) {
    /**
     * Execute account deletion
     * WARNING: This is a destructive operation that cannot be undone
     * @return Result indicating success or error
     */
    suspend operator fun invoke(): Result<Unit> {
        Timber.w("Executing account deletion - this cannot be undone")
        return userRepository.deleteAccount()
    }
}