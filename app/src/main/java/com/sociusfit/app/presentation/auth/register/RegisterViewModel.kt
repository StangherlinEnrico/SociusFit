package com.sociusfit.app.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.app.core.Constants
import com.sociusfit.app.core.util.Result
import com.sociusfit.app.core.util.UiState
import com.sociusfit.app.core.util.isValidEmail
import com.sociusfit.app.core.util.isValidPassword
import com.sociusfit.app.data.remote.dto.RegisterResponse
import com.sociusfit.app.domain.usecase.auth.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<RegisterResponse>>(UiState.Idle)
    val uiState: StateFlow<UiState<RegisterResponse>> = _uiState.asStateFlow()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError: StateFlow<String?> = _confirmPasswordError.asStateFlow()

    fun onNameChange(newName: String) {
        _name.value = newName
        _nameError.value = null
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        _emailError.value = null
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        _passwordError.value = null
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
        _confirmPasswordError.value = null
    }

    fun register() {
        if (!validateInputs()) return

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = registerUseCase(_name.value, _email.value, _password.value)) {
                is Result.Success -> {
                    _uiState.value = UiState.Success(result.data)
                }

                is Result.Error -> {
                    _uiState.value = UiState.Error(
                        result.message ?: result.exception.localizedMessage
                        ?: "Errore durante la registrazione",
                        result.exception
                    )
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        if (_name.value.isBlank()) {
            _nameError.value = "Il nome è obbligatorio"
            isValid = false
        } else if (_name.value.length < Constants.MIN_NAME_LENGTH) {
            _nameError.value =
                "Il nome deve contenere almeno ${Constants.MIN_NAME_LENGTH} caratteri"
            isValid = false
        } else if (_name.value.length > Constants.MAX_NAME_LENGTH) {
            _nameError.value =
                "Il nome deve contenere massimo ${Constants.MAX_NAME_LENGTH} caratteri"
            isValid = false
        }

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
            _passwordError.value =
                "Password non valida (minimo ${Constants.MIN_PASSWORD_LENGTH} caratteri, almeno una lettera e un numero)"
            isValid = false
        }

        if (_confirmPassword.value.isBlank()) {
            _confirmPasswordError.value = "Conferma la password"
            isValid = false
        } else if (_password.value != _confirmPassword.value) {
            _confirmPasswordError.value = "Le password non coincidono"
            isValid = false
        }

        return isValid
    }

    fun resetUiState() {
        _uiState.value = UiState.Idle
    }
}