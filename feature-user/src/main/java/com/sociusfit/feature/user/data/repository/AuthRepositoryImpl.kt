package com.sociusfit.feature.user.data.repository

import com.sociusfit.feature.user.data.local.TokenDataSource
import com.sociusfit.feature.user.data.mapper.toDomain
import com.sociusfit.feature.user.data.remote.api.AuthApiService
import com.sociusfit.feature.user.data.remote.dto.LoginRequestDto
import com.sociusfit.feature.user.data.remote.dto.RegisterRequestDto
import com.sociusfit.feature.user.domain.model.Result
import com.sociusfit.feature.user.domain.model.User
import com.sociusfit.feature.user.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class AuthRepositoryImpl(
    private val authApiService: AuthApiService,
    private val tokenDataSource: TokenDataSource
) : AuthRepository {

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<User> {
        return try {
            val response = authApiService.register(
                RegisterRequestDto(firstName, lastName, email, password)
            )

            if (response.isSuccessful && response.body()?.isSuccess == true) {
                val authResponse = response.body()!!.data!!
                tokenDataSource.saveToken(authResponse.token)
                Result.Success(authResponse.user.toDomain())
            } else {
                val errors = response.body()?.errors ?: listOf("Errore durante la registrazione")
                Result.Error(errors.firstOrNull() ?: "Errore durante la registrazione")
            }
        } catch (e: Exception) {
            Timber.e(e, "Register error")
            Result.Error(e.message ?: "Errore di connessione")
        }
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = authApiService.login(LoginRequestDto(email, password))

            if (response.isSuccessful && response.body()?.isSuccess == true) {
                val authResponse = response.body()!!.data!!
                tokenDataSource.saveToken(authResponse.token)
                Result.Success(authResponse.user.toDomain())
            } else {
                val errors = response.body()?.errors ?: listOf("Email o password non corretti")
                Result.Error(errors.firstOrNull() ?: "Email o password non corretti")
            }
        } catch (e: Exception) {
            Timber.e(e, "Login error")
            Result.Error(e.message ?: "Errore di connessione")
        }
    }

    override suspend fun getCurrentUser(): Result<User> {
        return try {
            val response = authApiService.getCurrentUser()

            if (response.isSuccessful && response.body()?.isSuccess == true) {
                Result.Success(response.body()!!.data!!.toDomain())
            } else {
                Result.Error("Sessione scaduta")
            }
        } catch (e: Exception) {
            Timber.e(e, "Get current user error")
            Result.Error(e.message ?: "Errore di connessione")
        }
    }

    override suspend fun saveToken(token: String) {
        tokenDataSource.saveToken(token)
    }

    override suspend fun getToken(): String? {
        return tokenDataSource.getToken()
    }

    override suspend fun clearToken() {
        tokenDataSource.clearToken()
    }

    override fun isAuthenticated(): Flow<Boolean> {
        return tokenDataSource.isAuthenticated()
    }
}