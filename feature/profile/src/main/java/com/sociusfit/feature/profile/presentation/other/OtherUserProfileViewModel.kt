package com.sociusfit.feature.profile.presentation.other

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.core.domain.Result
import com.sociusfit.feature.profile.domain.model.Profile
import com.sociusfit.feature.profile.domain.usecase.GetProfileByUserIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel per visualizzare il profilo di un altro utente
 */
class OtherUserProfileViewModel(
    private val getProfileByUserIdUseCase: GetProfileByUserIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OtherUserProfileUiState())
    val uiState: StateFlow<OtherUserProfileUiState> = _uiState.asStateFlow()

    fun loadProfile(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getProfileByUserIdUseCase(userId)) {
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

data class OtherUserProfileUiState(
    val profile: Profile? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)