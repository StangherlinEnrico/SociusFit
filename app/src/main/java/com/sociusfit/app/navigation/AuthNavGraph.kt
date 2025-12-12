package com.sociusfit.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sociusfit.app.ui.auth.LoginScreen
import com.sociusfit.app.ui.auth.RegisterScreen
import com.sociusfit.app.ui.auth.SplashScreen
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

/**
 * Auth Navigation Graph - FIXED
 *
 * Gestisce la navigazione del modulo Auth.
 * Flow: Splash → Login/Register → Profile/Onboarding
 */
fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    onNavigateToProfile: () -> Unit,  // ← CAMBIATO da onNavigateToHome
    onNavigateToOnboarding: () -> Unit
) {
    navigation(
        startDestination = AppRoutes.SPLASH,
        route = AppRoutes.AUTH_GRAPH
    ) {

        // ============================================
        // SPLASH SCREEN
        // ============================================
        composable(AppRoutes.SPLASH) {
            Timber.d("Navigate to SplashScreen")

            SplashScreen(
                viewModel = koinViewModel(),
                onNavigateToLogin = {
                    Timber.d("Splash → Login")
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToProfile = {  // ← CAMBIATO da onNavigateToHome
                    Timber.d("Splash → Profile (token valid, profile complete)")
                    onNavigateToProfile()
                },
                onNavigateToOnboarding = {
                    Timber.d("Splash → Onboarding (profile incomplete)")
                    onNavigateToOnboarding()
                }
            )
        }

        // ============================================
        // LOGIN SCREEN
        // ============================================
        composable(AppRoutes.LOGIN) {
            Timber.d("Navigate to LoginScreen")

            LoginScreen(
                viewModel = koinViewModel(),
                onNavigateToRegister = {
                    Timber.d("Login → Register")
                    navController.navigate(AppRoutes.REGISTER)
                },
                onNavigateToProfile = {  // ← CAMBIATO da onNavigateToHome
                    Timber.d("Login → Profile (profile complete)")
                    onNavigateToProfile()
                },
                onNavigateToOnboarding = {
                    Timber.d("Login → Onboarding (profile incomplete)")
                    onNavigateToOnboarding()
                }
            )
        }

        // ============================================
        // REGISTER SCREEN
        // ============================================
        composable(AppRoutes.REGISTER) {
            Timber.d("Navigate to RegisterScreen")

            RegisterScreen(
                viewModel = koinViewModel(),
                onNavigateToLogin = {
                    Timber.d("Register → Login (back)")
                    navController.popBackStack()
                },
                onNavigateToOnboarding = {
                    Timber.d("Register → Onboarding")
                    onNavigateToOnboarding()
                }
            )
        }
    }
}