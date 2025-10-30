package com.sociusfit.app.presentation.navigation

import androidx.compose.runtime.Composable
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
        // Splash Screen
        composable(Routes.SPLASH) {
            // TODO: Implementare SplashScreen
            // SplashScreen(navController = navController)
        }

        // Auth Flow
        composable(Routes.LOGIN) {
            // TODO: Implementare LoginScreen
            // LoginScreen(navController = navController)
        }

        composable(Routes.REGISTER) {
            // TODO: Implementare RegisterScreen
            // RegisterScreen(navController = navController)
        }

        composable(Routes.PROFILE_SETUP) {
            // TODO: Implementare ProfileSetupScreen
            // ProfileSetupScreen(navController = navController)
        }

        // Main Flow
        composable(Routes.MATCH) {
            // TODO: Implementare MatchScreen
            // MatchScreen(navController = navController)
        }

        composable(Routes.MATCH_SEARCH) {
            // TODO: Implementare MatchSearchScreen
            // MatchSearchScreen(navController = navController)
        }

        composable(Routes.PROFILE) {
            // TODO: Implementare ProfileScreen
            // ProfileScreen(navController = navController)
        }

        composable(Routes.PROFILE_EDIT) {
            // TODO: Implementare ProfileEditScreen
            // ProfileEditScreen(navController = navController)
        }

        composable(Routes.NOTIFICATIONS) {
            // TODO: Implementare NotificationsScreen
            // NotificationsScreen(navController = navController)
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

fun NavHostController.navigateToProfileSetup() {
    navigate(Routes.PROFILE_SETUP) {
        popUpTo(Routes.REGISTER) { inclusive = true }
    }
}

fun NavHostController.navigateToMatch() {
    navigate(Routes.MATCH) {
        popUpTo(Routes.PROFILE_SETUP) { inclusive = true }
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