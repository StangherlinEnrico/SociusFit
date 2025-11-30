package com.sociusfit.app.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.model.User
import com.sociusfit.app.domain.usecase.auth.LogoutUseCase
import com.sociusfit.app.domain.usecase.location.GetMunicipalityByCodeUseCase
import com.sociusfit.app.domain.usecase.user.GetCurrentUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * 🔥 UPDATED: ViewModel per ProfileScreen con logout
 */
class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getMunicipalityByCodeUseCase: GetMunicipalityByCodeUseCase,
    private val logoutUseCase: LogoutUseCase  // 🔥 NEW
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    /**
     * Carica profilo utente e risolve il codice località
     */
    fun loadUserProfile() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            Timber.d("Loading user profile")

            when (val result = getCurrentUserUseCase()) {
                is Result.Success -> {
                    Timber.i("User profile loaded successfully")
                    val user = result.data

                    // Risolvi il codice località in nome visualizzato
                    val locationDisplayName = if (user.location != null) {
                        when (val municipalityResult = getMunicipalityByCodeUseCase(user.location)) {
                            is Result.Success -> {
                                val municipality = municipalityResult.data
                                "${municipality.name} (${municipality.regionName})"
                            }
                            else -> {
                                Timber.w("Failed to resolve location code: ${user.location}")
                                user.location
                            }
                        }
                    } else {
                        null
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = user,
                            locationDisplayName = locationDisplayName
                        )
                    }
                }

                is Result.Error -> {
                    Timber.w("Failed to load user profile: ${result.message}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Errore sconosciuto durante il caricamento del profilo"
                        )
                    }
                }
            }
        }
    }

    /**
     * 🔥 NEW: Logout from device
     */
    fun logout() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            Timber.d("Logging out user")

            when (val result = logoutUseCase()) {
                is Result.Success -> {
                    Timber.i("Logout successful")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            logoutSuccess = true
                        )
                    }
                }

                is Result.Error -> {
                    Timber.w("Logout failed: ${result.message}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Errore sconosciuto durante il logout"
                        )
                    }
                }
            }
        }
    }
}

/**
 * 🔥 UPDATED: UI State con logoutSuccess flag
 */
data class ProfileUiState(
    val user: User? = null,
    val locationDisplayName: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val logoutSuccess: Boolean = false  // 🔥 NEW
)