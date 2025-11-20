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
            com.sociusfit.app.presentation.auth.login.LoginScreen(navController = navController)
        }

        composable(Routes.REGISTER) {
            com.sociusfit.app.presentation.auth.register.RegisterScreen(navController = navController)
        }

        composable(Routes.FORGOT_PASSWORD) {
            com.sociusfit.app.presentation.auth.forgotpassword.ForgotPasswordScreen(navController = navController)
        }
    }
}

fun NavHostController.navigateToLogin() {
    navigate(Routes.LOGIN) {
        popUpTo(Routes.SPLASH) { inclusive = true }
    }
}

fun NavHostController.navigateToRegister() {
    navigate(Routes.REGISTER) {
        popUpTo(Routes.SPLASH) { inclusive = true }
    }
}

fun NavHostController.navigateToForgotPassword() {
    navigate(Routes.FORGOT_PASSWORD) {
        popUpTo(Routes.SPLASH) { inclusive = true }
    }
}
