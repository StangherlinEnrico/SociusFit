package com.sociusfit.app.domain.usecase.auth

import com.sociusfit.app.core.util.Result
import com.sociusfit.app.data.remote.dto.RegisterResponse
import com.sociusfit.app.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): Result<RegisterResponse> {
        return repository.register(name, email, password)
    }
}