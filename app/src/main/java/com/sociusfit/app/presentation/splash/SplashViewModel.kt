package com.sociusfit.app.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.feature.user.data.local.TokenDataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SplashViewModel(
    private val tokenDataSource: TokenDataSource
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination.asStateFlow()

    init {
        checkAuthenticationStatus()
    }

    private fun checkAuthenticationStatus() {
        viewModelScope.launch {
            try {
                // Delay minimo per mostrare lo splash
                delay(1500)

                val token = tokenDataSource.getToken()

                _destination.value = if (token != null) {
                    Timber.d("Token found, navigating to Profile")
                    SplashDestination.Profile
                } else {
                    Timber.d("No token found, navigating to Login")
                    SplashDestination.Login
                }
            } catch (e: Exception) {
                Timber.e(e, "Error checking authentication status")
                _destination.value = SplashDestination.Login
            }
        }
    }
}