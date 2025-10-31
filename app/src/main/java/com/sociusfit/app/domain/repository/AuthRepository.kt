package com.sociusfit.app.domain.repository

import com.sociusfit.app.core.util.Result
import com.sociusfit.app.data.remote.dto.LoginResponse
import com.sociusfit.app.data.remote.dto.RegisterResponse
import com.sociusfit.app.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<LoginResponse>
    suspend fun register(name: String, email: String, password: String): Result<RegisterResponse>
    suspend fun refreshToken(): Result<String>
    suspend fun validateToken(): Result<Boolean>
    suspend fun logout(): Result<Unit>
    suspend fun getCurrentUser(): Result<User?>
}