package com.sociusfit.feature.user.domain.usecase.auth

import com.sociusfit.feature.user.domain.model.Result
import com.sociusfit.feature.user.domain.model.User
import com.sociusfit.feature.user.domain.repository.AuthRepository

class GetCurrentUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Result<User> = repository.getCurrentUser()
}