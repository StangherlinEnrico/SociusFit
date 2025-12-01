package com.sociusfit.app.presentation.splash

sealed class SplashDestination {
    data object Login : SplashDestination()
    data object Profile : SplashDestination()
}