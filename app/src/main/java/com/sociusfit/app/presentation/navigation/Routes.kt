package com.sociusfit.app.presentation.navigation

/**
 * Costanti per le routes di navigazione
 */
object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot_password"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"
}

/**
 * Sealed class per le destinazioni di navigazione
 */
sealed class NavigationDestination(val route: String) {
    object Splash : NavigationDestination(Routes.SPLASH)
    object Login : NavigationDestination(Routes.LOGIN)
    object Register : NavigationDestination(Routes.REGISTER)
    object ForgotPassword : NavigationDestination(Routes.FORGOT_PASSWORD)
    object Profile : NavigationDestination(Routes.PROFILE)
    object EditProfile : NavigationDestination(Routes.EDIT_PROFILE)
}