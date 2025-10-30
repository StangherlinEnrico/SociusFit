package com.sociusfit.app.presentation.navigation

/**
 * Oggetto contenente tutte le route di navigazione dell'app
 */
object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PROFILE_SETUP = "profile_setup"
    const val MATCH = "match"
    const val MATCH_SEARCH = "match_search"
    const val PROFILE = "profile"
    const val PROFILE_EDIT = "profile_edit"
    const val NOTIFICATIONS = "notifications"

    // Route con argomenti
    object Args {
        const val USER_ID = "userId"
        const val MATCH_ID = "matchId"
    }

    // Helper per costruire route con argomenti
    fun matchDetails(matchId: String) = "match_details/$matchId"
    fun userProfile(userId: String) = "user_profile/$userId"
}

/**
 * Sealed class per gestire la navigazione in modo type-safe
 */
sealed class NavigationDestination(val route: String) {
    object Splash : NavigationDestination(Routes.SPLASH)
    object Login : NavigationDestination(Routes.LOGIN)
    object Register : NavigationDestination(Routes.REGISTER)
    object ProfileSetup : NavigationDestination(Routes.PROFILE_SETUP)
    object Match : NavigationDestination(Routes.MATCH)
    object MatchSearch : NavigationDestination(Routes.MATCH_SEARCH)
    object Profile : NavigationDestination(Routes.PROFILE)
    object ProfileEdit : NavigationDestination(Routes.PROFILE_EDIT)
    object Notifications : NavigationDestination(Routes.NOTIFICATIONS)
}