package com.sociusfit.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sociusfit.app.presentation.auth.forgotpassword.ForgotPasswordScreen
import com.sociusfit.app.presentation.auth.login.LoginScreen
import com.sociusfit.app.presentation.auth.register.RegisterScreen
import com.sociusfit.app.presentation.profile.ProfileScreen
import com.sociusfit.app.presentation.splash.SplashScreen

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
            SplashScreen(navController = navController)
        }

        composable(Routes.LOGIN) {
            LoginScreen(navController = navController)
        }

        composable(Routes.REGISTER) {
            RegisterScreen(navController = navController)
        }

        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(navController = navController)
        }

        composable(Routes.PROFILE) {
            ProfileScreen(navController = navController)
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

fun NavHostController.navigateToForgotPassword() {
    navigate(Routes.FORGOT_PASSWORD)
}

fun NavHostController.navigateToProfile() {
    navigate(Routes.PROFILE) {
        popUpTo(0) { inclusive = true }
    }
}