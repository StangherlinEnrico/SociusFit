package com.sociusfit.app.domain.usecase.auth

import com.sociusfit.app.core.util.Result
import com.sociusfit.app.data.remote.dto.LoginResponse
import com.sociusfit.app.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<LoginResponse> {
        return repository.login(email, password)
    }
}