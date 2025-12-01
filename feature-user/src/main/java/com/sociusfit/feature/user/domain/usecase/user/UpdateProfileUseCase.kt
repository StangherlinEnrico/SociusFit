package com.sociusfit.feature.user.domain.usecase.user

import com.sociusfit.feature.user.domain.model.Result
import com.sociusfit.feature.user.domain.model.User
import com.sociusfit.feature.user.domain.repository.UserRepository

class UpdateProfileUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(firstName: String, lastName: String): Result<User> {
        if (firstName.isBlank() || firstName.length < 2) {
            return Result.Error("Il nome deve avere almeno 2 caratteri")
        }
        if (lastName.isBlank() || lastName.length < 2) {
            return Result.Error("Il cognome deve avere almeno 2 caratteri")
        }

        return repository.updateProfile(firstName, lastName)
    }
}