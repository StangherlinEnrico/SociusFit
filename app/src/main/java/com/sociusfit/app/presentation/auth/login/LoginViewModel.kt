package com.sociusfit.app.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.app.core.util.Result
import com.sociusfit.app.core.util.UiState
import com.sociusfit.app.core.util.isValidEmail
import com.sociusfit.app.core.util.isValidPassword
import com.sociusfit.app.data.remote.dto.LoginResponse
import com.sociusfit.app.domain.usecase.auth.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<LoginResponse>>(UiState.Idle)
    val uiState: StateFlow<UiState<LoginResponse>> = _uiState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        _emailError.value = null
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        _passwordError.value = null
    }

    fun login() {
        if (!validateInputs()) return

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = loginUseCase(_email.value, _password.value)) {
                is Result.Success -> {
                    _uiState.value = UiState.Success(result.data)
                }

                is Result.Error -> {
                    _uiState.value = UiState.Error(
                        result.message ?: result.exception.localizedMessage
                        ?: "Errore durante il login",
                        result.exception
                    )
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        if (_email.value.isBlank()) {
            _emailError.value = "L'email è obbligatoria"
            isValid = false
        } else if (!_email.value.isValidEmail()) {
            _emailError.value = "Email non valida"
            isValid = false
        }

        if (_password.value.isBlank()) {
            _passwordError.value = "La password è obbligatoria"
            isValid = false
        } else if (!_password.value.isValidPassword()) {
            _passwordError.value = "Password non valida (minimo 8 caratteri)"
            isValid = false
        }

        return isValid
    }

    fun resetUiState() {
        _uiState.value = UiState.Idle
    }
}