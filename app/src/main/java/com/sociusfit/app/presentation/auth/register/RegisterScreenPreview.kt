package com.sociusfit.app.presentation.auth.register

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.sociusfit.app.domain.model.AuthResponse
import com.sociusfit.app.domain.model.OAuthProvider
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.model.User
import com.sociusfit.app.domain.repository.AuthRepository
import com.sociusfit.app.domain.usecase.auth.LoginWithOAuthUseCase
import com.sociusfit.app.domain.usecase.auth.RegisterUseCase
import com.sociusfit.app.presentation.theme.SociusFitTheme
import java.time.LocalDateTime

/**
 * Mock AuthRepository per Preview
 */
private class MockAuthRepository : AuthRepository {
    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        return Result.Success(AuthResponse(
            token = "mock_token",
            expiresAt = LocalDateTime.now().plusHours(1),
            user = User(
                id = 1,
                firstName = "Mario",
                lastName = "Rossi",
                email = "mario@test.com",
                isEmailVerified = true,
                provider = null,
                createdAt = LocalDateTime.now()
            )
        ))
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<AuthResponse> {
        return Result.Success(AuthResponse(
            token = "mock_token",
            expiresAt = LocalDateTime.now().plusHours(1),
            user = User(
                id = 1,
                firstName = firstName,
                lastName = lastName,
                email = email,
                isEmailVerified = false,
                provider = null,
                createdAt = LocalDateTime.now()
            )
        ))
    }

    override suspend fun loginWithOAuth(
        provider: OAuthProvider,
        token: String
    ): Result<AuthResponse> {
        return Result.Success(AuthResponse(
            token = "mock_token",
            expiresAt = LocalDateTime.now().plusHours(1),
            user = User(
                id = 1,
                firstName = "Mario",
                lastName = "Rossi",
                email = "mario@test.com",
                isEmailVerified = true,
                provider = provider.providerName,
                createdAt = LocalDateTime.now()
            )
        ))
    }

    override suspend fun logout(): Result<Unit> = Result.Success(Unit)
    override suspend fun isLoggedIn(): Boolean = false
    override suspend fun getAuthToken(): String? = null
}

/**
 * Preview della RegisterScreen
 */
@Preview(
    name = "Register Screen - Light Mode",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun RegisterScreenPreview() {
    val mockAuthRepository = MockAuthRepository()
    val mockViewModel = RegisterViewModel(
        registerUseCase = RegisterUseCase(mockAuthRepository),
        loginWithOAuthUseCase = LoginWithOAuthUseCase(mockAuthRepository)
    )

    SociusFitTheme {
        RegisterScreen(
            navController = rememberNavController(),
            viewModel = mockViewModel
        )
    }
}

/**
 * Preview della RegisterScreen in modalità scura
 */
@Preview(
    name = "Register Screen - Dark Mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun RegisterScreenDarkPreview() {
    val mockAuthRepository = MockAuthRepository()
    val mockViewModel = RegisterViewModel(
        registerUseCase = RegisterUseCase(mockAuthRepository),
        loginWithOAuthUseCase = LoginWithOAuthUseCase(mockAuthRepository)
    )

    SociusFitTheme(darkTheme = true) {
        RegisterScreen(
            navController = rememberNavController(),
            viewModel = mockViewModel
        )
    }
}
