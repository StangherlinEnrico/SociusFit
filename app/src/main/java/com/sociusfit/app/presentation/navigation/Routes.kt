package com.sociusfit.app.presentation.navigation

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PROFILE_SETUP = "profile_setup"
}

sealed class NavigationDestination(val route: String) {
    object Splash : NavigationDestination(Routes.SPLASH)
    object Login : NavigationDestination(Routes.LOGIN)
    object Register : NavigationDestination(Routes.REGISTER)
    object ProfileSetup : NavigationDestination(Routes.PROFILE_SETUP)
}