package com.sociusfit.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sociusfit.app.ui.auth.LoginScreen
import com.sociusfit.app.ui.auth.RegisterScreen
import com.sociusfit.app.ui.auth.SplashScreen
import com.sociusfit.feature.auth.presentation.login.LoginViewModel
import com.sociusfit.feature.auth.presentation.register.RegisterViewModel
import com.sociusfit.feature.auth.presentation.splash.SplashViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Auth Navigation Graph
 *
 * Gestisce la navigazione del modulo Auth.
 * Le Screen Composable sono in :app, i ViewModel sono iniettati da Koin qui.
 */
fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    onNavigateToHome: () -> Unit,
    onNavigateToOnboarding: () -> Unit
) {
    navigation(
        startDestination = "splash",
        route = "auth"
    ) {
        // Splash Screen
        composable("splash") {
            val viewModel = koinViewModel<SplashViewModel>()

            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToHome = onNavigateToHome,
                onNavigateToOnboarding = onNavigateToOnboarding,
                viewModel = viewModel
            )
        }

        // Login Screen
        composable("login") {
            val viewModel = koinViewModel<LoginViewModel>()

            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onNavigateToHome = onNavigateToHome,
                onNavigateToOnboarding = onNavigateToOnboarding,
                viewModel = viewModel
            )
        }

        // Register Screen
        composable("register") {
            val viewModel = koinViewModel<RegisterViewModel>()

            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToOnboarding = onNavigateToOnboarding,
                viewModel = viewModel
            )
        }
    }
}