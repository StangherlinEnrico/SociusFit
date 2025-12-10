package com.sociusfit.feature.profile.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.core.domain.Result
import com.sociusfit.feature.profile.domain.model.Profile
import com.sociusfit.feature.profile.domain.usecase.GetMyProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel per visualizzare il profilo dell'utente corrente
 */
class ProfileViewModel(
    private val getMyProfileUseCase: GetMyProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getMyProfileUseCase()) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            profile = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                }
                is Result.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }
}

data class ProfileUiState(
    val profile: Profile? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)