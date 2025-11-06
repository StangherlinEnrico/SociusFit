package com.sociusfit.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
        composable(Routes.SPLASH) {
            com.sociusfit.app.presentation.splash.SplashScreen(navController = navController)
        }

        composable(Routes.LOGIN) {
            // TODO: Implementare pagina di Login
        }

        composable(Routes.REGISTER) {
            // TODO: Implementare pagina di registrazione
        }

        composable(Routes.PROFILE_SETUP) {
            // TODO: Implementare ProfileSetupScreen
        }
    }
}

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
