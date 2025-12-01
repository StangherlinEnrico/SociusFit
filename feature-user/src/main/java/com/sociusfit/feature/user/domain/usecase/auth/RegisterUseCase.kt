package com.sociusfit.feature.user.domain.usecase.auth

import com.sociusfit.feature.user.domain.model.Result
import com.sociusfit.feature.user.domain.model.User
import com.sociusfit.feature.user.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<User> {
        if (firstName.isBlank() || firstName.length < 2) {
            return Result.Error("Il nome deve avere almeno 2 caratteri")
        }
        if (lastName.isBlank() || lastName.length < 2) {
            return Result.Error("Il cognome deve avere almeno 2 caratteri")
        }
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.Error("Email non valida")
        }
        if (password.length < 8) {
            return Result.Error("La password deve avere almeno 8 caratteri")
        }

        return repository.register(firstName, lastName, email, password)
    }
}