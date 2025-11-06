package com.sociusfit.app.domain.repository

import com.sociusfit.app.domain.model.AuthResponse
import com.sociusfit.app.domain.model.OAuthProvider
import com.sociusfit.app.domain.model.Result

/**
 * Repository interface for authentication operations
 */
interface AuthRepository {

    /**
     * Login with email and password
     * @param email User email
     * @param password User password
     * @return Result with AuthResponse or error
     */
    suspend fun login(email: String, password: String): Result<AuthResponse>

    /**
     * Register a new user
     * @param firstName User first name
     * @param lastName User last name
     * @param email User email
     * @param password User password
     * @return Result with AuthResponse or error
     */
    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<AuthResponse>

    /**
     * Login with OAuth provider
     * @param provider OAuth provider (Google, Facebook, Microsoft, Apple)
     * @param token OAuth token from provider
     * @return Result with AuthResponse or error
     */
    suspend fun loginWithOAuth(
        provider: OAuthProvider,
        token: String
    ): Result<AuthResponse>

    /**
     * Logout current user
     * Clears local session data
     */
    suspend fun logout(): Result<Unit>

    /**
     * Check if user is currently logged in
     * @return true if user has valid session, false otherwise
     */
    suspend fun isLoggedIn(): Boolean

    /**
     * Get current authentication token
     * @return Token string or null if not logged in
     */
    suspend fun getAuthToken(): String?
}