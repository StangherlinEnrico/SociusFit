//package com.sociusfit.app.presentation.profile.edit
//
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.navigation.compose.rememberNavController
//import com.sociusfit.app.data.local.DataStoreManager
//import com.sociusfit.app.domain.model.Municipality
//import com.sociusfit.app.domain.model.Region
//import com.sociusfit.app.domain.model.Result
//import com.sociusfit.app.domain.model.User
//import com.sociusfit.app.domain.repository.LocationRepository
//import com.sociusfit.app.domain.repository.UserRepository
//import com.sociusfit.app.domain.usecase.location.SearchMunicipalitiesUseCase
//import com.sociusfit.app.domain.usecase.user.GetCurrentUserUseCase
//import com.sociusfit.app.domain.usecase.user.UpdateProfileUseCase
//import com.sociusfit.app.presentation.theme.SociusFitTheme
//import java.time.LocalDateTime
//
///**
// * Mock UserRepository per Preview
// */
//private class MockUserRepository : UserRepository {
//    override suspend fun getCurrentUser(): Result<User> {
//        return Result.Success(
//            User(
//                id = 1,
//                firstName = "Mario",
//                lastName = "Rossi",
//                email = "mario.rossi@test.com",
//                isEmailVerified = true,
//                provider = null,
//                createdAt = LocalDateTime.now()
//            )
//        )
//    }
//
//    override suspend fun getUserById(userId: Int): Result<User> = getCurrentUser()
//
//    override suspend fun updateProfile(
//        firstName: String,
//        lastName: String,
//        location: String?
//    ): Result<User> {
//        return Result.Success(
//            User(
//                id = 1,
//                firstName = firstName,
//                lastName = lastName,
//                email = "mario.rossi@test.com",
//                isEmailVerified = true,
//                provider = null,
//                createdAt = LocalDateTime.now()
//            )
//        )
//    }
//
//    override suspend fun updateLocation(
//        latitude: Double,
//        longitude: Double,
//        maxDistance: Int
//    ): Result<User> = getCurrentUser()
//
//    override suspend fun deleteAccount(): Result<Unit> = Result.Success(Unit)
//}
//
///**
// * Mock LocationRepository per Preview
// */
//private class MockLocationRepository : LocationRepository {
//    override suspend fun getAllRegions(): Result<List<Region>> {
//        return Result.Success(emptyList())
//    }
//
//    override suspend fun getAllMunicipalities(): Result<List<Municipality>> {
//        return Result.Success(emptyList())
//    }
//
//    override suspend fun getRegionByCode(regionCode: String): Result<Region> {
//        return Result.Error("Not implemented")
//    }
//
//    override suspend fun getMunicipalitiesByRegion(regionCode: String): Result<List<Municipality>> {
//        return Result.Success(emptyList())
//    }
//
//    override suspend fun searchMunicipalities(query: String): Result<List<Municipality>> {
//        // Mock search results
//        val results = listOf(
//            Municipality("001001", "Milano", "03", "Lombardia"),
//            Municipality("001002", "Milanello", "03", "Lombardia"),
//            Municipality("001003", "Milazzo", "19", "Sicilia")
//        )
//        return Result.Success(results)
//    }
//
//    override suspend fun getMunicipalityByCode(municipalityCode: String): Result<Municipality> {
//        return Result.Error("Not implemented")
//    }
//}
//
///**
// * Preview della EditProfileScreen
// */
//@Preview(
//    name = "Edit Profile Screen - Light Mode",
//    showBackground = true,
//    showSystemUi = true
//)
//@Composable
//fun EditProfileScreenPreview() {
//    val context = LocalContext.current
//    val mockUserRepository = MockUserRepository()
//    val mockLocationRepository = MockLocationRepository()
//    val mockDataStoreManager = DataStoreManager(context)  // 🔥 NEW
//
//    val mockViewModel = EditProfileViewModel(
//        getCurrentUserUseCase = GetCurrentUserUseCase(mockUserRepository),
//        updateProfileUseCase = UpdateProfileUseCase(mockUserRepository),
//        searchMunicipalitiesUseCase = SearchMunicipalitiesUseCase(mockLocationRepository),
//        dataStoreManager = mockDataStoreManager  // 🔥 NEW
//    )
//
//    SociusFitTheme {
//        EditProfileScreen(
//            navController = rememberNavController(),
//            viewModel = mockViewModel
//        )
//    }
//}
//
///**
// * Preview della EditProfileScreen in modalità scura
// */
//@Preview(
//    name = "Edit Profile Screen - Dark Mode",
//    showBackground = true,
//    showSystemUi = true,
//    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun EditProfileScreenDarkPreview() {
//    val context = LocalContext.current
//    val mockUserRepository = MockUserRepository()
//    val mockLocationRepository = MockLocationRepository()
//    val mockDataStoreManager = DataStoreManager(context)  // 🔥 NEW
//
//    val mockViewModel = EditProfileViewModel(
//        getCurrentUserUseCase = GetCurrentUserUseCase(mockUserRepository),
//        updateProfileUseCase = UpdateProfileUseCase(mockUserRepository),
//        searchMunicipalitiesUseCase = SearchMunicipalitiesUseCase(mockLocationRepository),
//        dataStoreManager = mockDataStoreManager  // 🔥 NEW
//    )
//
//    SociusFitTheme(darkTheme = true) {
//        EditProfileScreen(
//            navController = rememberNavController(),
//            viewModel = mockViewModel
//        )
//    }
//}