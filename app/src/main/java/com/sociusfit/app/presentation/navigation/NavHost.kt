package com.sociusfit.app.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * NavHost principale dell'applicazione
 * Gestisce tutta la navigazione tra le schermate
 *
 * @param modifier Modifier opzionale
 * @param navController Controller per la navigazione
 * @param startDestination Route di partenza
 */
@Composable
fun SociusFitNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.SPLASH
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Splash Screen - ✅ IMPLEMENTATO
        composable(Routes.SPLASH) {
            com.sociusfit.app.presentation.splash.SplashScreen(navController = navController)
        }

        // Auth Flow - ✅ IMPLEMENTATO
        composable(Routes.LOGIN) {
            com.sociusfit.app.presentation.auth.login.LoginScreen(navController = navController)
        }

        composable(Routes.REGISTER) {
            com.sociusfit.app.presentation.auth.register.RegisterScreen(navController = navController)
        }

        composable(Routes.PROFILE_SETUP) {
            // TODO: Implementare ProfileSetupScreen (Fase 2)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Profile Setup - Coming Soon (Fase 2)",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        // Main Flow
        composable(Routes.MATCH) {
            // TODO: Implementare MatchScreen (Fase 3) - Per ora placeholder
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Match Screen - Coming Soon (Fase 3)",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        composable(Routes.MATCH_SEARCH) {
            // TODO: Implementare MatchSearchScreen (Fase 3)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Match Search - Coming Soon (Fase 3)",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        composable(Routes.PROFILE) {
            // TODO: Implementare ProfileScreen (Fase 2)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Profile - Coming Soon (Fase 2)",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        composable(Routes.PROFILE_EDIT) {
            // TODO: Implementare ProfileEditScreen (Fase 2)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Profile Edit - Coming Soon (Fase 2)",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        composable(Routes.NOTIFICATIONS) {
            // TODO: Implementare NotificationsScreen (Fase 4)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Notifications - Coming Soon (Fase 4)",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

/**
 * Extension functions per navigazione type-safe
 */
fun NavHostController.navigateToLogin() {
    navigate(Routes.LOGIN) {
        popUpTo(Routes.SPLASH) { inclusive = true }
    }
}

fun NavHostController.navigateToRegister() {
    navigate(Routes.REGISTER)
}

fun NavHostController.navigateToProfileSetup() {
    navigate(Routes.PROFILE_SETUP) {
        popUpTo(Routes.REGISTER) { inclusive = true }
    }
}

fun NavHostController.navigateToMatch() {
    navigate(Routes.MATCH) {
        popUpTo(0) { inclusive = true }
    }
}

fun NavHostController.navigateToMatchSearch() {
    navigate(Routes.MATCH_SEARCH)
}

fun NavHostController.navigateToProfile() {
    navigate(Routes.PROFILE)
}

fun NavHostController.navigateToProfileEdit() {
    navigate(Routes.PROFILE_EDIT)
}

fun NavHostController.navigateToNotifications() {
    navigate(Routes.NOTIFICATIONS)
}