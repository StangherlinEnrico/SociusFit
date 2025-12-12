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
 *
 * AGGIORNATO: Usa OnboardingRepository per gestire lo stato temporaneo
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
            onContinue = {
                // I dati sono già salvati in OnboardingRepository
                navController.navigate(ProfileRoutes.ONBOARDING_SPORTS)
            }
        )
    }

    // Onboarding Sports - Step 2
    composable(ProfileRoutes.ONBOARDING_SPORTS) {
        OnboardingSportsScreen(
            onContinue = {
                // I dati sono già salvati in OnboardingRepository
                navController.navigate(ProfileRoutes.ONBOARDING_PHOTO)
            },
            onBack = {
                navController.popBackStack()
            }
        )
    }

    // Onboarding Photo - Step 3
    composable(ProfileRoutes.ONBOARDING_PHOTO) {
        // Il ViewModel leggerà i dati da OnboardingRepository
        OnboardingPhotoScreen(
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
    navigate(ProfileRoutes.PROFILE) {
        // Opzionale: clear back stack se necessario
        // popUpTo(0) { inclusive = false }
    }
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