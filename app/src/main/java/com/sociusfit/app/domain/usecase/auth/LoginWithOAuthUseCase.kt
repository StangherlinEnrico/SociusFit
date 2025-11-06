package com.sociusfit.app.domain.usecase.auth

import com.sociusfit.app.domain.model.AuthResponse
import com.sociusfit.app.domain.model.OAuthProvider
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.repository.AuthRepository
import timber.log.Timber

/**
 * Use case for OAuth login (Google, Facebook, Microsoft, Apple)
 * Handles validation and business logic for OAuth authentication
 */
class LoginWithOAuthUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Execute OAuth login
     * @param provider OAuth provider (Google, Facebook, Microsoft, Apple)
     * @param token OAuth token from provider
     * @return Result with AuthResponse or error
     */
    suspend operator fun invoke(
        provider: OAuthProvider,
        token: String
    ): Result<AuthResponse> {
        // Validate token
        if (token.isBlank()) {
            Timber.w("OAuth login failed: Token is empty")
            return Result.Error("OAuth token cannot be empty")
        }

        // Log provider
        Timber.d("OAuth login with provider: ${provider.providerName}")

        // Proceed with OAuth login
        return authRepository.loginWithOAuth(provider, token)
    }

    /**
     * Execute OAuth login with provider name string
     * Convenience method that converts string to OAuthProvider enum
     */
    suspend operator fun invoke(
        providerName: String,
        token: String
    ): Result<AuthResponse> {
        // Convert provider name to enum
        val provider = OAuthProvider.fromString(providerName)

        if (provider == null) {
            Timber.w("OAuth login failed: Unsupported provider: $providerName")
            return Result.Error("Unsupported OAuth provider: $providerName")
        }

        return invoke(provider, token)
    }
}