package com.sociusfit.app.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.sociusfit.app.domain.model.Municipality
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.model.User
import com.sociusfit.app.domain.repository.AuthRepository
import com.sociusfit.app.domain.repository.LocationRepository
import com.sociusfit.app.domain.repository.UserRepository
import com.sociusfit.app.domain.usecase.auth.LogoutUseCase
import com.sociusfit.app.domain.usecase.location.GetMunicipalityByCodeUseCase
import com.sociusfit.app.domain.usecase.user.GetCurrentUserUseCase
import com.sociusfit.app.presentation.theme.SociusFitTheme
import java.time.LocalDateTime

/**
 * Mock UserRepository per Preview
 */
private class MockUserRepository(
    private val shouldLoad: Boolean = true,
    private val hasLocation: Boolean = true
) : UserRepository {
    override suspend fun getCurrentUser(): Result<User> {
        if (!shouldLoad) {
            kotlinx.coroutines.delay(Long.MAX_VALUE)
        }

        return Result.Success(
            User(
                id = 1,
                firstName = "Mario",
                lastName = "Rossi",
                email = "mario.rossi@example.com",
                isEmailVerified = true,
                provider = "Google",
                location = if (hasLocation) "026086" else null, // Codice Treviso
                maxDistance = if (hasLocation) 25 else null,
                createdAt = LocalDateTime.now()
            )
        )
    }

    override suspend fun getUserById(userId: Int): Result<User> = getCurrentUser()

    override suspend fun updateProfile(
        firstName: String,
        lastName: String
    ): Result<User> = getCurrentUser()

    override suspend fun updateLocation(
        locationCode: String?,
        maxDistance: Int?
    ): Result<User> = getCurrentUser()

    override suspend fun deleteAccount(): Result<Unit> = Result.Success(Unit)
}

/**
 * Mock LocationRepository per Preview
 */
private class MockLocationRepository : LocationRepository {
    override suspend fun getAllRegions(): Result<List<com.sociusfit.app.domain.model.Region>> {
        return Result.Success(emptyList())
    }

    override suspend fun getAllMunicipalities(): Result<List<Municipality>> {
        return Result.Success(emptyList())
    }

    override suspend fun getRegionByCode(regionCode: String): Result<com.sociusfit.app.domain.model.Region> {
        return Result.Error("Not implemented")
    }

    override suspend fun getMunicipalitiesByRegion(regionCode: String): Result<List<Municipality>> {
        return Result.Success(emptyList())
    }

    override suspend fun searchMunicipalities(query: String): Result<List<Municipality>> {
        return Result.Success(emptyList())
    }

    override suspend fun getMunicipalityByCode(municipalityCode: String): Result<Municipality> {
        // Mock per Treviso
        if (municipalityCode == "026086") {
            return Result.Success(
                Municipality(
                    code = "026086",
                    name = "Treviso",
                    regionCode = "05",
                    regionName = "Veneto"
                )
            )
        }
        return Result.Error("Municipality not found")
    }
}

/**
 * 🔥 NEW: Mock AuthRepository per Preview
 */
private class MockAuthRepository : AuthRepository {
    override suspend fun login(email: String, password: String): Result<com.sociusfit.app.domain.model.AuthResponse> {
        return Result.Error("Not implemented in preview")
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<com.sociusfit.app.domain.model.AuthResponse> {
        return Result.Error("Not implemented in preview")
    }

    override suspend fun loginWithOAuth(
        provider: com.sociusfit.app.domain.model.OAuthProvider,
        token: String
    ): Result<com.sociusfit.app.domain.model.AuthResponse> {
        return Result.Error("Not implemented in preview")
    }

    override suspend fun logout(): Result<Unit> {
        // Mock logout sempre success nella preview
        return Result.Success(Unit)
    }

    override suspend fun isLoggedIn(): Boolean = true

    override suspend fun getAuthToken(): String? = "mock_token"

    override suspend fun forgotPassword(email: String): Result<String> {
        return Result.Error("Not implemented in preview")
    }
}

// ============================================================
// PREVIEW: TEMA CHIARO CON LOCALITÀ
// ============================================================
@Preview(
    name = "Profile Screen - Light Mode with Location",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ProfileScreenLightPreview() {
    val mockUserRepository = MockUserRepository(shouldLoad = true, hasLocation = true)
    val mockLocationRepository = MockLocationRepository()
    val mockAuthRepository = MockAuthRepository()

    val mockViewModel = ProfileViewModel(
        getCurrentUserUseCase = GetCurrentUserUseCase(mockUserRepository),
        getMunicipalityByCodeUseCase = GetMunicipalityByCodeUseCase(mockLocationRepository),
        logoutUseCase = LogoutUseCase(mockAuthRepository)  // 🔥 ADD
    )

    SociusFitTheme(darkTheme = false) {
        ProfileScreen(
            navController = rememberNavController(),
            viewModel = mockViewModel
        )
    }
}

// ============================================================
// PREVIEW: TEMA SCURO CON LOCALITÀ
// ============================================================
@Preview(
    name = "Profile Screen - Dark Mode with Location",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ProfileScreenDarkPreview() {
    val mockUserRepository = MockUserRepository(shouldLoad = true, hasLocation = true)
    val mockLocationRepository = MockLocationRepository()
    val mockAuthRepository = MockAuthRepository()

    val mockViewModel = ProfileViewModel(
        getCurrentUserUseCase = GetCurrentUserUseCase(mockUserRepository),
        getMunicipalityByCodeUseCase = GetMunicipalityByCodeUseCase(mockLocationRepository),
        logoutUseCase = LogoutUseCase(mockAuthRepository)  // 🔥 ADD
    )

    SociusFitTheme(darkTheme = true) {
        ProfileScreen(
            navController = rememberNavController(),
            viewModel = mockViewModel
        )
    }
}

// ============================================================
// PREVIEW: LOADING - TEMA CHIARO
// ============================================================
@Preview(
    name = "Profile Screen - Loading Light",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ProfileScreenLoadingLightPreview() {
    val mockUserRepository = MockUserRepository(shouldLoad = false, hasLocation = true)
    val mockLocationRepository = MockLocationRepository()
    val mockAuthRepository = MockAuthRepository()

    val mockViewModel = ProfileViewModel(
        getCurrentUserUseCase = GetCurrentUserUseCase(mockUserRepository),
        getMunicipalityByCodeUseCase = GetMunicipalityByCodeUseCase(mockLocationRepository),
        logoutUseCase = LogoutUseCase(mockAuthRepository)  // 🔥 ADD
    )

    SociusFitTheme(darkTheme = false) {
        ProfileScreen(
            navController = rememberNavController(),
            viewModel = mockViewModel
        )
    }
}

// ============================================================
// PREVIEW: LOADING - TEMA SCURO
// ============================================================
@Preview(
    name = "Profile Screen - Loading Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ProfileScreenLoadingDarkPreview() {
    val mockUserRepository = MockUserRepository(shouldLoad = false, hasLocation = true)
    val mockLocationRepository = MockLocationRepository()
    val mockAuthRepository = MockAuthRepository()

    val mockViewModel = ProfileViewModel(
        getCurrentUserUseCase = GetCurrentUserUseCase(mockUserRepository),
        getMunicipalityByCodeUseCase = GetMunicipalityByCodeUseCase(mockLocationRepository),
        logoutUseCase = LogoutUseCase(mockAuthRepository)  // 🔥 ADD
    )

    SociusFitTheme(darkTheme = true) {
        ProfileScreen(
            navController = rememberNavController(),
            viewModel = mockViewModel
        )
    }
}

// ============================================================
// PREVIEW: SENZA LOCALITÀ - TEMA CHIARO
// ============================================================
@Preview(
    name = "Profile Screen - No Location Light",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ProfileScreenNoLocationLightPreview() {
    val mockUserRepository = MockUserRepository(shouldLoad = true, hasLocation = false)
    val mockLocationRepository = MockLocationRepository()
    val mockAuthRepository = MockAuthRepository()

    val mockViewModel = ProfileViewModel(
        getCurrentUserUseCase = GetCurrentUserUseCase(mockUserRepository),
        getMunicipalityByCodeUseCase = GetMunicipalityByCodeUseCase(mockLocationRepository),
        logoutUseCase = LogoutUseCase(mockAuthRepository)  // 🔥 ADD
    )

    SociusFitTheme(darkTheme = false) {
        ProfileScreen(
            navController = rememberNavController(),
            viewModel = mockViewModel
        )
    }
}

// ============================================================
// PREVIEW: SENZA LOCALITÀ - TEMA SCURO
// ============================================================
@Preview(
    name = "Profile Screen - No Location Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ProfileScreenNoLocationDarkPreview() {
    val mockUserRepository = MockUserRepository(shouldLoad = true, hasLocation = false)
    val mockLocationRepository = MockLocationRepository()
    val mockAuthRepository = MockAuthRepository()

    val mockViewModel = ProfileViewModel(
        getCurrentUserUseCase = GetCurrentUserUseCase(mockUserRepository),
        getMunicipalityByCodeUseCase = GetMunicipalityByCodeUseCase(mockLocationRepository),
        logoutUseCase = LogoutUseCase(mockAuthRepository)  // 🔥 ADD
    )

    SociusFitTheme(darkTheme = true) {
        ProfileScreen(
            navController = rememberNavController(),
            viewModel = mockViewModel
        )
    }
}