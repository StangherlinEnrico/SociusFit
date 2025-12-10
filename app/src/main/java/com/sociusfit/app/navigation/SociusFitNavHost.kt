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
 * SociusFit Navigation Host
 *
 * Entry point della navigazione dell'app.
 * Responsabilità:
 * - Gestisce il NavHost principale
 * - Collega tutti i navigation graphs (Auth, Profile, Discovery, Match, Chat)
 * - Determina la start destination
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
            onNavigateToHome = {
                Timber.d("Navigate to Home (profile complete)")
                // Se profileComplete = true → Profile screen
                // TODO Sprint 3: Implement Home with bottom navigation
                navController.navigateToProfile()
            },
            onNavigateToOnboarding = {
                Timber.d("Navigate to Onboarding (profile incomplete)")
                // Se profileComplete = false → Onboarding
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
                // Pulisci lo stato di autenticazione
                AuthStateManager.clearUser()
                // Naviga direttamente a Login (non Splash!)
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