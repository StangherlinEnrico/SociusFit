package com.sociusfit.feature.user.domain.repository

import com.sociusfit.feature.user.domain.model.Result
import com.sociusfit.feature.user.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(firstName: String, lastName: String, email: String, password: String): Result<User>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun getCurrentUser(): Result<User>
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()
    fun isAuthenticated(): Flow<Boolean>
}