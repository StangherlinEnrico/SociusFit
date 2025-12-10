package com.sociusfit.app.ui.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sociusfit.app.ui.profile.EditProfileScreen
import com.sociusfit.app.ui.profile.OtherUserProfileScreen
import com.sociusfit.app.ui.profile.ProfileScreen
import com.sociusfit.app.ui.profile.onboarding.OnboardingBioScreen
import com.sociusfit.app.ui.profile.onboarding.OnboardingPhotoScreen
import com.sociusfit.app.ui.profile.onboarding.OnboardingSportsScreen
import com.sociusfit.feature.profile.domain.model.ProfileSport
import com.sociusfit.feature.profile.domain.model.SportLevel
import com.sociusfit.feature.profile.presentation.onboarding.photo.ProfileData

/**
 * Routes per Profile module
 */
object ProfileRoutes {
    const val ONBOARDING_BIO = "profile/onboarding/bio"
    const val ONBOARDING_SPORTS = "profile/onboarding/sports"
    const val ONBOARDING_PHOTO = "profile/onboarding/photo"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "profile/edit"
    const val OTHER_USER_PROFILE = "profile/user/{userId}"

    fun otherUserProfile(userId: String) = "profile/user/$userId"
}

/**
 * Extension per aggiungere navigation al Profile module
 */
fun NavGraphBuilder.addProfileNavigation(
    navController: NavHostController,
    startDestination: String = ProfileRoutes.PROFILE,
    onLogout: () -> Unit,
    getUserId: () -> String,
    getUserName: () -> String
) {
    // Onboarding Bio - Step 1
    composable(ProfileRoutes.ONBOARDING_BIO) {
        OnboardingBioScreen(
            onContinue = { age, city, bio, maxDistance ->
                // Salva dati temporanei
                navController.currentBackStackEntry?.savedStateHandle?.apply {
                    set("age", age)
                    set("city", city)
                    set("bio", bio)
                    set("maxDistance", maxDistance)
                }
                navController.navigate(ProfileRoutes.ONBOARDING_SPORTS)
            }
        )
    }

    // Onboarding Sports - Step 2
    composable(ProfileRoutes.ONBOARDING_SPORTS) {
        OnboardingSportsScreen(
            onContinue = { selectedSports ->
                // Salva sport selezionati
                navController.currentBackStackEntry?.savedStateHandle?.apply {
                    set("selectedSports", HashMap(selectedSports))
                }
                navController.navigate(ProfileRoutes.ONBOARDING_PHOTO)
            },
            onBack = {
                navController.popBackStack()
            }
        )
    }

    // Onboarding Photo - Step 3
    composable(ProfileRoutes.ONBOARDING_PHOTO) {
        // Recupera dati dai step precedenti
        val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
        val age = savedStateHandle?.get<Int>("age") ?: 25
        val city = savedStateHandle?.get<String>("city") ?: ""
        val bio = savedStateHandle?.get<String>("bio") ?: ""
        val maxDistance = savedStateHandle?.get<Int>("maxDistance") ?: 25

        @Suppress("UNCHECKED_CAST")
        val selectedSportsMap = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<HashMap<String, SportLevel>>("selectedSports") ?: hashMapOf()

        val profileData = ProfileData(
            userId = getUserId(),
            firstName = getUserName().split(" ").firstOrNull() ?: "",
            lastName = getUserName().split(" ").drop(1).joinToString(" "),
            age = age,
            city = city,
            latitude = 0.0, // Backend calcolerà con geocoding
            longitude = 0.0,
            bio = bio,
            maxDistance = maxDistance,
            sports = selectedSportsMap.map { (sportId, level) ->
                ProfileSport(
                    sportId = sportId,
                    sportName = "", // Backend popolerà
                    level = level
                )
            }
        )

        OnboardingPhotoScreen(
            profileData = profileData,
            onComplete = {
                // Vai a profile, rimuovi tutto lo stack onboarding
                navController.navigate(ProfileRoutes.PROFILE) {
                    popUpTo(ProfileRoutes.ONBOARDING_BIO) { inclusive = true }
                }
            },
            onBack = {
                navController.popBackStack()
            }
        )
    }

    // Profile Screen
    composable(ProfileRoutes.PROFILE) {
        ProfileScreen(
            onEditProfile = {
                navController.navigate(ProfileRoutes.EDIT_PROFILE)
            },
            onLogout = onLogout
        )
    }

    // Edit Profile Screen
    composable(ProfileRoutes.EDIT_PROFILE) {
        EditProfileScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }

    // Other User Profile Screen
    composable(
        route = ProfileRoutes.OTHER_USER_PROFILE,
        arguments = listOf(
            navArgument("userId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val userId = backStackEntry.arguments?.getString("userId") ?: return@composable

        OtherUserProfileScreen(
            userId = userId,
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}

/**
 * Extension functions per navigazione type-safe
 */
fun NavHostController.navigateToProfile() {
    navigate(ProfileRoutes.PROFILE)
}

fun NavHostController.navigateToEditProfile() {
    navigate(ProfileRoutes.EDIT_PROFILE)
}

fun NavHostController.navigateToOtherUserProfile(userId: String) {
    navigate(ProfileRoutes.otherUserProfile(userId))
}

fun NavHostController.navigateToOnboarding() {
    navigate(ProfileRoutes.ONBOARDING_BIO) {
        // Clear back stack per evitare loop
        popUpTo(0) { inclusive = false }
    }
}