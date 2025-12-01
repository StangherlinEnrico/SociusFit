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
            Timber.d("🔵 Register request: email=$email, firstName=$firstName")

            val response = authApiService.register(
                RegisterRequestDto(firstName, lastName, email, password)
            )

            Timber.d("🔵 Response code: ${response.code()}")
            Timber.d("🔵 Response successful: ${response.isSuccessful}")

            if (response.isSuccessful) {
                val authResponse = response.body()

                if (authResponse == null) {
                    Timber.e("❌ Response body is null!")
                    return Result.Error("Risposta del server non valida")
                }

                Timber.d("🔵 Token: ${authResponse.token.take(20)}...")
                Timber.d("🔵 User: ${authResponse.user.email}")

                if (authResponse.token.isBlank()) {
                    Timber.e("❌ Token is blank!")
                    return Result.Error("Token non valido ricevuto dal server")
                }

                // Salva token
                tokenDataSource.saveToken(authResponse.token)

                // Converti user in domain model
                val user = authResponse.user.toDomain()

                Timber.d("✅ Registration successful: user=${user.email}")
                Result.Success(user)

            } else {
                // Errore HTTP
                val errorMsg = when (response.code()) {
                    400 -> "Dati non validi"
                    409 -> "Email già registrata"
                    500 -> "Errore del server"
                    else -> "Errore durante la registrazione"
                }

                Timber.e("❌ HTTP ${response.code()}: $errorMsg")
                Result.Error(errorMsg)
            }
        } catch (e: Exception) {
            Timber.e(e, "💥 Register exception")
            Result.Error(e.message ?: "Errore di connessione")
        }
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            Timber.d("🔵 Login request: email=$email")

            val response = authApiService.login(LoginRequestDto(email, password))

            Timber.d("🔵 Response code: ${response.code()}")
            Timber.d("🔵 Response successful: ${response.isSuccessful}")

            if (response.isSuccessful) {
                val authResponse = response.body()

                if (authResponse == null) {
                    Timber.e("❌ Response body is null!")
                    return Result.Error("Risposta del server non valida")
                }

                // Salva token
                tokenDataSource.saveToken(authResponse.token)

                // Converti user in domain model
                val user = authResponse.user.toDomain()

                Timber.d("✅ Login successful: user=${user.email}")
                Result.Success(user)

            } else {
                val errorMsg = when (response.code()) {
                    401 -> "Email o password non corretti"
                    404 -> "Utente non trovato"
                    500 -> "Errore del server"
                    else -> "Errore durante il login"
                }

                Timber.e("❌ HTTP ${response.code()}: $errorMsg")
                Result.Error(errorMsg)
            }
        } catch (e: Exception) {
            Timber.e(e, "💥 Login exception")
            Result.Error(e.message ?: "Errore di connessione")
        }
    }

    override suspend fun getCurrentUser(): Result<User> {
        return try {
            Timber.d("🔵 Get current user request")

            val response = authApiService.getCurrentUser()

            if (response.isSuccessful) {
                val userDto = response.body()

                if (userDto == null) {
                    Timber.e("❌ User data is null!")
                    return Result.Error("Dati utente non disponibili")
                }

                val user = userDto.toDomain()
                Timber.d("✅ Current user: ${user.email}")

                Result.Success(user)
            } else {
                val errorMsg = when (response.code()) {
                    401 -> "Sessione scaduta"
                    404 -> "Utente non trovato"
                    else -> "Errore nel recupero utente"
                }

                Timber.e("❌ HTTP ${response.code()}: $errorMsg")
                Result.Error(errorMsg)
            }
        } catch (e: Exception) {
            Timber.e(e, "💥 Get current user exception")
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