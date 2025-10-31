package com.sociusfit.app.domain.usecase.auth

import com.sociusfit.app.core.util.Result
import com.sociusfit.app.domain.repository.AuthRepository

class ValidateTokenUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Result<Boolean> {
        return repository.validateToken()
    }
}