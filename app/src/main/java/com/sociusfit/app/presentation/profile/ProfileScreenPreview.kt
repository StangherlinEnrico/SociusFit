package com.sociusfit.app.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.model.User
import com.sociusfit.app.domain.repository.UserRepository
import com.sociusfit.app.domain.usecase.user.GetCurrentUserUseCase
import com.sociusfit.app.presentation.theme.SociusFitTheme
import java.time.LocalDateTime

/**
 * Mock UserRepository per Preview
 */
private class MockUserRepository : UserRepository {
    override suspend fun getCurrentUser(): Result<User> {
        return Result.Success(
            User(
                id = 1,
                firstName = "Mario",
                lastName = "Rossi",
                email = "mario.rossi@test.com",
                isEmailVerified = true,
                provider = null,
                createdAt = LocalDateTime.now()
            )
        )
    }

    override suspend fun getUserById(userId: Int): Result<User> {
        return Result.Success(
            User(
                id = userId,
                firstName = "Mario",
                lastName = "Rossi",
                email = "mario.rossi@test.com",
                isEmailVerified = true,
                provider = null,
                createdAt = LocalDateTime.now()
            )
        )
    }

    override suspend fun updateProfile(
        firstName: String,
        lastName: String,
        location: String?
    ): Result<User> {
        return Result.Success(
            User(
                id = 1,
                firstName = firstName,
                lastName = lastName,
                email = "mario.rossi@test.com",
                isEmailVerified = true,
                provider = null,
                createdAt = LocalDateTime.now()
            )
        )
    }

    override suspend fun updateLocation(
        latitude: Double,
        longitude: Double,
        maxDistanceKm: Int
    ): Result<User> {
        return Result.Success(
            User(
                id = 1,
                firstName = "Mario",
                lastName = "Rossi",
                email = "mario.rossi@test.com",
                isEmailVerified = true,
                provider = null,
                createdAt = LocalDateTime.now()
            )
        )
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return Result.Success(Unit)
    }
}

/**
 * Preview della ProfileScreen
 */
@Preview(
    name = "Profile Screen - Light Mode",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ProfileScreenPreview() {
    val mockUserRepository = MockUserRepository()
    val mockViewModel = ProfileViewModel(
        getCurrentUserUseCase = GetCurrentUserUseCase(mockUserRepository)
    )

    SociusFitTheme {
        ProfileScreen(
            navController = rememberNavController(),
            viewModel = mockViewModel
        )
    }
}

/**
 * Preview della ProfileScreen in modalità scura
 */
@Preview(
    name = "Profile Screen - Dark Mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ProfileScreenDarkPreview() {
    val mockUserRepository = MockUserRepository()
    val mockViewModel = ProfileViewModel(
        getCurrentUserUseCase = GetCurrentUserUseCase(mockUserRepository)
    )

    SociusFitTheme(darkTheme = true) {
        ProfileScreen(
            navController = rememberNavController(),
            viewModel = mockViewModel
        )
    }
}

/**
 * Preview della ProfileScreen con loading
 */
@Preview(
    name = "Profile Screen - Loading",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ProfileScreenLoadingPreview() {
    // Mock che ritorna sempre loading (ritardo infinito)
    val mockUserRepository = object : UserRepository {
        override suspend fun getCurrentUser(): Result<User> {
            kotlinx.coroutines.delay(Long.MAX_VALUE)
            return Result.Success(
                User(
                    id = 1,
                    firstName = "Mario",
                    lastName = "Rossi",
                    email = "test@test.com",
                    isEmailVerified = true,
                    provider = null,
                    createdAt = LocalDateTime.now()
                )
            )
        }

        override suspend fun getUserById(userId: Int): Result<User> = getCurrentUser()
        override suspend fun updateProfile(firstName: String, lastName: String, location: String?): Result<User> = getCurrentUser()
        override suspend fun updateLocation(latitude: Double, longitude: Double, maxDistanceKm: Int): Result<User> = getCurrentUser()
        override suspend fun deleteAccount(): Result<Unit> = Result.Success(Unit)
    }

    val mockViewModel = ProfileViewModel(
        getCurrentUserUseCase = GetCurrentUserUseCase(mockUserRepository)
    )

    SociusFitTheme {
        ProfileScreen(
            navController = rememberNavController(),
            viewModel = mockViewModel
        )
    }
}