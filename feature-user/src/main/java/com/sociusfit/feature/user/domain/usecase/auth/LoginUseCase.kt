package com.sociusfit.feature.user.domain.usecase.auth

import com.sociusfit.feature.user.domain.model.Result
import com.sociusfit.feature.user.domain.model.User
import com.sociusfit.feature.user.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.Error("Email non valida")
        }
        if (password.isBlank()) {
            return Result.Error("La password è obbligatoria")
        }

        return repository.login(email, password)
    }
}