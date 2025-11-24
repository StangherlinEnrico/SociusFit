package com.sociusfit.app.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.app.domain.usecase.auth.CheckLoginStatusUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

sealed class SplashDestination {
    object Login : SplashDestination()
    object Profile : SplashDestination()
}

class SplashViewModel(
    private val checkLoginStatusUseCase: CheckLoginStatusUseCase
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination.asStateFlow()

    init {
        checkAuthenticationStatus()
    }

    private fun checkAuthenticationStatus() {
        viewModelScope.launch {
            // Delay per mostrare splash screen
            delay(1500)

            Timber.d("Checking authentication status")

            // Controlla se l'utente ha un token salvato
            val isLoggedIn = checkLoginStatusUseCase()

            _destination.value = if (isLoggedIn) {
                Timber.i("User is logged in, navigating to Profile")
                SplashDestination.Profile
            } else {
                Timber.i("User is not logged in, navigating to Login")
                SplashDestination.Login
            }
        }
    }
}