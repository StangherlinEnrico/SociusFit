package com.sociusfit.feature.auth.data.repository

import com.sociusfit.core.domain.Result
import com.sociusfit.core.domain.model.User
import com.sociusfit.core.network.api.AuthApiService
import com.sociusfit.core.network.dto.auth.LoginRequest
import com.sociusfit.core.network.dto.auth.RegisterRequest
import com.sociusfit.core.storage.PreferencesManager
import com.sociusfit.core.storage.dao.UserDao
import com.sociusfit.feature.auth.data.mapper.toUser
import com.sociusfit.feature.auth.data.mapper.toUserEntity
import com.sociusfit.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

/**
 * Auth Repository Implementation
 *
 * Con logging completo e gestione corretta degli errori HTTP.
 */
class AuthRepositoryImpl(
    private val apiService: AuthApiService,
    private val preferencesManager: PreferencesManager,
    private val userDao: UserDao
) : AuthRepository {

    companion object {
        private const val TAG = "AuthRepository"
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<User> {
        return try {
            Timber.tag(TAG).d("━━━ REGISTER ━━━")
            Timber.tag(TAG).d("Email: $email")

            val response = apiService.register(
                request = RegisterRequest(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = password
                )
            )

            Timber.tag(TAG).d("✓ API call successful")
            Timber.tag(TAG).d("Token received: ${response.token.take(20)}...")

            preferencesManager.saveToken(response.token)
            Timber.tag(TAG).d("✓ Token saved")

            val user = response.user.toUser()
            userDao.insertUser(user.toUserEntity())
            Timber.tag(TAG).d("✓ User saved to database")
            Timber.tag(TAG).d("━━━━━━━━━━━━━━━━")

            Result.Success(user)

        } catch (e: HttpException) {
            Timber.tag(TAG).e(e, "✗ Register failed - HTTP ${e.code()}")

            val userMessage = when (e.code()) {
                409 -> "Email già registrata"
                400 -> "Dati non validi. Controlla i campi."
                else -> "Errore durante la registrazione"
            }

            Result.Error(Exception(userMessage, e))

        } catch (e: IOException) {
            Timber.tag(TAG).e(e, "✗ Register failed - Network error")
            Result.Error(Exception("Errore di connessione. Verifica la tua rete.", e))

        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "✗ Register failed - Unexpected error")
            Result.Error(Exception("Errore imprevisto durante la registrazione", e))
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<User> {
        return try {
            Timber.tag(TAG).d("━━━ LOGIN ━━━")
            Timber.tag(TAG).d("Email: $email")

            val response = apiService.login(
                request = LoginRequest(
                    email = email,
                    password = password
                )
            )

            Timber.tag(TAG).d("✓ API call successful")
            Timber.tag(TAG).d("Token received: ${response.token.take(20)}...")

            preferencesManager.saveToken(response.token)
            Timber.tag(TAG).d("✓ Token saved")

            val user = response.user.toUser()
            userDao.insertUser(user.toUserEntity())
            Timber.tag(TAG).d("✓ User saved to database")
            Timber.tag(TAG).d("━━━━━━━━━━━━━━━━")

            Result.Success(user)

        } catch (e: HttpException) {
            Timber.tag(TAG).w(e, "✗ Login failed - HTTP ${e.code()}")

            val userMessage = when (e.code()) {
                401 -> "Email o password errate"
                400 -> "Dati non validi"
                else -> "Errore durante il login"
            }

            Result.Error(Exception(userMessage, e))

        } catch (e: IOException) {
            Timber.tag(TAG).e(e, "✗ Login failed - Network error")
            Result.Error(Exception("Errore di connessione. Verifica la tua rete.", e))

        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "✗ Login failed - Unexpected error")
            Result.Error(Exception("Errore imprevisto durante il login", e))
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            Timber.tag(TAG).d("━━━ LOGOUT ━━━")

            apiService.logout()
            Timber.tag(TAG).d("✓ API call successful")

            preferencesManager.clearAuth()
            Timber.tag(TAG).d("✓ Auth cleared from preferences")

            userDao.deleteAllUsers()
            Timber.tag(TAG).d("✓ Users deleted from database")
            Timber.tag(TAG).d("━━━━━━━━━━━━━━━━")

            Result.Success(Unit)

        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "✗ Logout API failed, clearing local data anyway")

            try {
                preferencesManager.clearAuth()
                userDao.deleteAllUsers()
                Timber.tag(TAG).d("✓ Local data cleared")
            } catch (localError: Exception) {
                Timber.tag(TAG).e(localError, "✗ Failed to clear local data")
            }

            // Logout è sempre success anche se API fallisce
            Result.Success(Unit)
        }
    }

    override suspend fun getCurrentUser(): Result<User> {
        return try {
            Timber.tag(TAG).d("━━━ GET CURRENT USER ━━━")
            Timber.tag(TAG).d("Calling API /users/me...")

            val userDto = apiService.getCurrentUser()
            Timber.tag(TAG).d("✓ API call successful")
            Timber.tag(TAG).d("  User ID: ${userDto.id}")
            Timber.tag(TAG).d("  Email: ${userDto.email}")

            val user = userDto.toUser()

            userDao.insertUser(user.toUserEntity())
            Timber.tag(TAG).d("✓ User updated in database")
            Timber.tag(TAG).d("━━━━━━━━━━━━━━━━")

            Result.Success(user)

        } catch (e: HttpException) {
            Timber.tag(TAG).w(e, "✗ API call failed - HTTP ${e.code()}, trying local cache...")

            val localUser = userDao.getCurrentUser()
            if (localUser != null) {
                Timber.tag(TAG).d("✓ Found user in local cache")
                Timber.tag(TAG).d("  User ID: ${localUser.id}")
                Timber.tag(TAG).d("━━━━━━━━━━━━━━━━")
                Result.Success(localUser.toUser())
            } else {
                Timber.tag(TAG).e("✗ No user in local cache")
                Timber.tag(TAG).d("━━━━━━━━━━━━━━━━")

                val userMessage = when (e.code()) {
                    401 -> "Sessione scaduta. Effettua nuovamente il login."
                    else -> "Errore durante il recupero dei dati utente"
                }

                Result.Error(Exception(userMessage, e))
            }

        } catch (e: IOException) {
            Timber.tag(TAG).w(e, "✗ Network error, trying local cache...")

            val localUser = userDao.getCurrentUser()
            if (localUser != null) {
                Timber.tag(TAG).d("✓ Using cached user data")
                Result.Success(localUser.toUser())
            } else {
                Result.Error(Exception("Errore di connessione e nessun dato locale disponibile", e))
            }

        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "✗ Unexpected error")
            Result.Error(Exception("Errore imprevisto", e))
        }
    }

    override suspend fun isAuthenticated(): Boolean {
        return try {
            val token = preferencesManager.getToken()
            val result = !token.isNullOrBlank()
            Timber.tag(TAG).d("isAuthenticated: $result (token present: ${token != null})")
            result
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Error checking authentication")
            false
        }
    }

    override fun observeCurrentUser(): Flow<User?> {
        return userDao.observeCurrentUser().map { userEntity ->
            userEntity?.toUser()
        }
    }

    override suspend fun saveToken(token: String) {
        preferencesManager.saveToken(token)
    }

    override suspend fun getToken(): String? {
        return preferencesManager.getToken()
    }

    override suspend fun clearToken() {
        preferencesManager.clearAuth()
    }
}