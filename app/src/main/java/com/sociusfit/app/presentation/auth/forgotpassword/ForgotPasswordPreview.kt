package com.sociusfit.app.presentation.auth.forgotpassword

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.sociusfit.app.domain.model.AuthResponse
import com.sociusfit.app.domain.model.OAuthProvider
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.model.User
import com.sociusfit.app.domain.repository.AuthRepository
import com.sociusfit.app.domain.usecase.auth.ForgotPasswordUseCase
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
                email = email,
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

    override suspend fun forgotPassword(email: String): Result<String> {
        return Result.Success("Password reset email sent successfully to $email")
    }

    override suspend fun logout(): Result<Unit> = Result.Success(Unit)
    override suspend fun isLoggedIn(): Boolean = false
    override suspend fun getAuthToken(): String? = null
}

/**
 * Preview della ForgotPasswordScreen
 */
@Preview(
    name = "Forgot Password Screen - Light Mode",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ForgotPasswordScreenPreview() {
    val mockAuthRepository = MockAuthRepository()
    val mockViewModel = ForgotPasswordViewModel(
        forgotPasswordUseCase = ForgotPasswordUseCase(mockAuthRepository)
    )

    SociusFitTheme {
        ForgotPasswordScreen(
            navController = rememberNavController(),
            viewModel = mockViewModel
        )
    }
}

/**
 * Preview della ForgotPasswordScreen in modalità scura
 */
@Preview(
    name = "Forgot Password Screen - Dark Mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ForgotPasswordScreenDarkPreview() {
    val mockAuthRepository = MockAuthRepository()
    val mockViewModel = ForgotPasswordViewModel(
        forgotPasswordUseCase = ForgotPasswordUseCase(mockAuthRepository)
    )

    SociusFitTheme(darkTheme = true) {
        ForgotPasswordScreen(
            navController = rememberNavController(),
            viewModel = mockViewModel
        )
    }
}