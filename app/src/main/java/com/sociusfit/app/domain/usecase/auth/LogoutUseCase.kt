package com.sociusfit.app.domain.usecase.auth

import com.sociusfit.app.core.util.Result
import com.sociusfit.app.domain.repository.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.logout()
    }
}