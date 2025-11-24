package com.sociusfit.app.data.repository

import com.sociusfit.app.data.local.DataStoreManager
import com.sociusfit.app.data.remote.api.AuthApiService
import com.sociusfit.app.data.remote.api.ForgotPasswordRequestDto
import com.sociusfit.app.data.remote.dto.auth.LoginRequestDto
import com.sociusfit.app.data.remote.dto.auth.OAuthLoginRequestDto
import com.sociusfit.app.data.remote.dto.auth.RegisterRequestDto
import com.sociusfit.app.data.remote.mapper.AuthResponseMapper
import com.sociusfit.app.domain.model.AuthResponse
import com.sociusfit.app.domain.model.OAuthProvider
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import timber.log.Timber

/**
 * Implementation of AuthRepository
 * Handles authentication operations with backend API and local storage
 */
class AuthRepositoryImpl(
    private val authApiService: AuthApiService,
    private val dataStoreManager: DataStoreManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            Timber.d("Attempting login for email: $email")

            val response = authApiService.login(
                LoginRequestDto(
                    email = email,
                    password = password
                )
            )

            if (response.isSuccessful) {
                val apiResult = response.body()

                if (apiResult?.success == true && apiResult.data != null) {
                    val authResponse = AuthResponseMapper.toDomain(apiResult.data)

                    // Save token and user data to DataStore
                    dataStoreManager.saveAuthToken(authResponse.token)
                    dataStoreManager.saveUserData(
                        userId = authResponse.user.id,
                        email = authResponse.user.email,
                        firstName = authResponse.user.firstName,
                        lastName = authResponse.user.lastName
                    )

                    Timber.i("Login successful for user: ${authResponse.user.email}")
                    Result.Success(authResponse, apiResult.message)
                } else {
                    val errorMessage = apiResult?.message ?: "Login failed"
                    Timber.w("Login failed: $errorMessage")
                    Result.Error(errorMessage)
                }
            } else {
                val errorMessage = "HTTP ${response.code()}: ${response.message()}"
                Timber.e("Login request failed: $errorMessage")
                Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception during login")
            Result.Error(e.message ?: "Network error occurred")
        }
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<AuthResponse> {
        return try {
            Timber.d("Attempting registration for email: $email")

            val response = authApiService.register(
                RegisterRequestDto(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = password
                )
            )

            if (response.isSuccessful) {
                val apiResult = response.body()

                if (apiResult?.success == true && apiResult.data != null) {
                    val authResponse = AuthResponseMapper.toDomain(apiResult.data)

                    // Save token and user data to DataStore
                    dataStoreManager.saveAuthToken(authResponse.token)
                    dataStoreManager.saveUserData(
                        userId = authResponse.user.id,
                        email = authResponse.user.email,
                        firstName = authResponse.user.firstName,
                        lastName = authResponse.user.lastName
                    )

                    Timber.i("Registration successful for user: ${authResponse.user.email}")
                    Result.Success(authResponse, apiResult.message)
                } else {
                    val errorMessage = apiResult?.message ?: "Registration failed"
                    Timber.w("Registration failed: $errorMessage")
                    Result.Error(errorMessage)
                }
            } else {
                val errorMessage = "HTTP ${response.code()}: ${response.message()}"
                Timber.e("Registration request failed: $errorMessage")
                Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception during registration")
            Result.Error(e.message ?: "Network error occurred")
        }
    }

    override suspend fun loginWithOAuth(
        provider: OAuthProvider,
        token: String
    ): Result<AuthResponse> {
        return try {
            Timber.d("Attempting OAuth login with provider: ${provider.providerName}")

            val response = authApiService.loginWithOAuth(
                OAuthLoginRequestDto(
                    provider = provider.name,
                    token = token
                )
            )

            if (response.isSuccessful) {
                val apiResult = response.body()

                if (apiResult?.success == true && apiResult.data != null) {
                    val authResponse = AuthResponseMapper.toDomain(apiResult.data)

                    // Save token and user data to DataStore
                    dataStoreManager.saveAuthToken(authResponse.token)
                    dataStoreManager.saveUserData(
                        userId = authResponse.user.id,
                        email = authResponse.user.email,
                        firstName = authResponse.user.firstName,
                        lastName = authResponse.user.lastName
                    )

                    Timber.i("OAuth login successful for user: ${authResponse.user.email}")
                    Result.Success(authResponse, apiResult.message)
                } else {
                    val errorMessage = apiResult?.message ?: "OAuth login failed"
                    Timber.w("OAuth login failed: $errorMessage")
                    Result.Error(errorMessage)
                }
            } else {
                val errorMessage = "HTTP ${response.code()}: ${response.message()}"
                Timber.e("OAuth login request failed: $errorMessage")
                Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception during OAuth login")
            Result.Error(e.message ?: "Network error occurred")
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            Timber.d("Logging out user")

            // Clear all local data
            dataStoreManager.clearAll()

            Timber.i("Logout successful")
            Result.Success(Unit, "Logged out successfully")
        } catch (e: Exception) {
            Timber.e(e, "Exception during logout")
            Result.Error(e.message ?: "Logout failed")
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return try {
            val isLoggedIn = dataStoreManager.isLoggedIn.first()
            val token = dataStoreManager.authToken.first()

            // User is logged in if flag is true AND token exists
            val result = isLoggedIn && !token.isNullOrBlank()
            Timber.d("User logged in status: $result")
            result
        } catch (e: Exception) {
            Timber.e(e, "Exception checking login status")
            false
        }
    }

    override suspend fun getAuthToken(): String? {
        return try {
            val token = dataStoreManager.authToken.first()
            if (token.isNullOrBlank()) {
                Timber.w("No auth token available")
                null
            } else {
                Timber.d("Auth token retrieved")
                token
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception retrieving auth token")
            null
        }
    }

    override suspend fun forgotPassword(email: String): Result<String> {
        return try {
            Timber.d("Requesting password reset for email: $email")

            val request = ForgotPasswordRequestDto(email = email)
            val response = authApiService.forgotPassword(request)

            if (response.isSuccessful && response.body()?.success == true) {
                val message = response.body()?.data ?: "Password reset email sent successfully"
                Timber.i("Password reset email sent successfully")
                Result.Success(message)
            } else {
                val errorMessage = response.body()?.message ?: "Failed to send password reset email"
                Timber.w("Password reset request failed: $errorMessage")
                Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception during password reset request")
            Result.Error(e.message ?: "Network error occurred")
        }
    }
}