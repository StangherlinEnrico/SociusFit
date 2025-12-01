package com.sociusfit.feature.user.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.feature.user.domain.model.Result
import com.sociusfit.feature.user.domain.usecase.auth.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onFirstNameChanged(firstName: String) {
        _uiState.update { it.copy(firstName = firstName, firstNameError = null) }
    }

    fun onLastNameChanged(lastName: String) {
        _uiState.update { it.copy(lastName = lastName, lastNameError = null) }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, emailError = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, confirmPasswordError = null) }
    }

    fun onRegisterClick() {
        if (!validateForm()) return

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val state = _uiState.value
            when (val result = registerUseCase(
                state.firstName,
                state.lastName,
                state.email,
                state.password
            )) {
                is Result.Success -> {
                    Timber.i("Registration successful")
                    _uiState.update { it.copy(isLoading = false, registerSuccess = true) }
                }
                is Result.Error -> {
                    Timber.w("Registration failed: ${result.message}")
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is Result.Loading -> {}
            }
        }
    }

    private fun validateForm(): Boolean {
        val state = _uiState.value

        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(confirmPasswordError = "Le password non coincidono") }
            return false
        }

        return true
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}