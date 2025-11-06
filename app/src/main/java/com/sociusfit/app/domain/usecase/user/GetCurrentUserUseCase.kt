package com.sociusfit.app.domain.usecase.user

import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.model.User
import com.sociusfit.app.domain.repository.UserRepository
import timber.log.Timber

/**
 * Use case for getting current logged in user
 * Retrieves user data from local storage or remote API
 */
class GetCurrentUserUseCase(
    private val userRepository: UserRepository
) {
    /**
     * Execute get current user
     * @return Result with User or error
     */
    suspend operator fun invoke(): Result<User> {
        Timber.d("Getting current user")
        return userRepository.getCurrentUser()
    }
}