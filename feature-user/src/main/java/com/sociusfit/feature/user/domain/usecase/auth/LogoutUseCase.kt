package com.sociusfit.feature.user.domain.usecase.auth

import com.sociusfit.feature.user.domain.repository.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() {
        repository.clearToken()
    }
}