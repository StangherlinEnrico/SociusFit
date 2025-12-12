package com.sociusfit.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.sociusfit.app.ui.profile.navigation.addProfileNavigation
import com.sociusfit.app.ui.profile.navigation.navigateToOnboarding
import com.sociusfit.app.ui.profile.navigation.navigateToProfile
import timber.log.Timber

/**
 * SociusFit Navigation Host - FIXED
 *
 * Entry point della navigazione dell'app.
 */
@Composable
fun SociusFitNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    Timber.d("SociusFitNavHost initialized")

    NavHost(
        navController = navController,
        startDestination = AppRoutes.AUTH_GRAPH,
        modifier = modifier
    ) {

        // ============================================
        // AUTH NAVIGATION GRAPH
        // ============================================
        authNavGraph(
            navController = navController,
            onNavigateToProfile = {  // ← CAMBIATO da onNavigateToHome
                Timber.d("Navigate to Profile (profile complete)")
                navController.navigateToProfile()
            },
            onNavigateToOnboarding = {
                Timber.d("Navigate to Onboarding (profile incomplete)")
                navController.navigateToOnboarding()
            }
        )

        // ============================================
        // PROFILE NAVIGATION MODULE
        // ============================================
        addProfileNavigation(
            navController = navController,
            onLogout = {
                Timber.d("User logged out, clearing state and navigate to Login")
                AuthStateManager.clearUser()
                navController.navigate(AppRoutes.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            },
            getUserId = {
                AuthStateManager.getUserId()
            },
            getUserName = {
                AuthStateManager.getUserName()
            }
        )

        // TODO Sprint 3: Add Discovery Navigation
        // TODO Sprint 4: Add Match Navigation
        // TODO Sprint 5: Add Chat Navigation
    }
}