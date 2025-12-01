package com.sociusfit.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sociusfit.app.presentation.screens.LoginScreen
import com.sociusfit.app.presentation.screens.ProfileScreen
import com.sociusfit.app.presentation.screens.RegisterScreen
import com.sociusfit.app.presentation.screens.SplashScreen

@Composable
fun SociusFitNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(navController)
        }

        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }

        composable(Routes.REGISTER) {
            RegisterScreen(navController)
        }

        composable(Routes.PROFILE) {
            ProfileScreen(navController)
        }
    }
}