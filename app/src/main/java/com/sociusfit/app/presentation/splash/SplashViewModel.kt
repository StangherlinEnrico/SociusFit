package com.sociusfit.app.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.app.data.local.datastore.DataStoreManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SplashDestination {
    object Login : SplashDestination()
    object ProfileSetup : SplashDestination()
    object Match : SplashDestination()
}

class SplashViewModel(
    private val dataStore: DataStoreManager
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination.asStateFlow()

    init {
        checkAuthenticationStatus()
    }

    private fun checkAuthenticationStatus() {
        viewModelScope.launch {
            delay(1500)

            val isAuthenticated = dataStore.isAuthenticated()

            _destination.value = if (isAuthenticated) {
                SplashDestination.Match
            } else {
                SplashDestination.Login
            }
        }
    }
}