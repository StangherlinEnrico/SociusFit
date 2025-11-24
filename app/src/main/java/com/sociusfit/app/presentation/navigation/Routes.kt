package com.sociusfit.app.presentation.navigation

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot_password"
    const val PROFILE = "profile"
}

sealed class NavigationDestination(val route: String) {
    object Splash : NavigationDestination(Routes.SPLASH)
    object Login : NavigationDestination(Routes.LOGIN)
    object Register : NavigationDestination(Routes.REGISTER)
    object ForgotPassword : NavigationDestination(Routes.FORGOT_PASSWORD)
    object Profile : NavigationDestination(Routes.PROFILE)
}