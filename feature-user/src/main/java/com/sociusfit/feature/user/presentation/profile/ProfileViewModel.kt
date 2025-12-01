package com.sociusfit.feature.user.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.feature.user.domain.model.Result
import com.sociusfit.feature.user.domain.usecase.auth.GetCurrentUserUseCase
import com.sociusfit.feature.user.domain.usecase.auth.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            when (val result = getCurrentUserUseCase()) {
                is Result.Success -> {
                    Timber.i("User profile loaded")
                    _uiState.update { it.copy(user = result.data, isLoading = false) }
                }
                is Result.Error -> {
                    Timber.w("Failed to load profile: ${result.message}")
                    _uiState.update { it.copy(error = result.message, isLoading = false) }
                }
                is Result.Loading -> {}
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _uiState.update { it.copy(logoutSuccess = true) }
        }
    }
}