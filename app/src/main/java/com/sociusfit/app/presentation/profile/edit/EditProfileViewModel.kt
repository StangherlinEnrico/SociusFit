package com.sociusfit.app.presentation.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.usecase.location.SearchMunicipalitiesUseCase
import com.sociusfit.app.domain.usecase.user.GetCurrentUserUseCase
import com.sociusfit.app.domain.usecase.user.UpdateProfileUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel per la schermata di modifica profilo
 */
@OptIn(FlowPreview::class)
class EditProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val searchMunicipalitiesUseCase: SearchMunicipalitiesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val locationQueryFlow = MutableStateFlow("")

    init {
        loadCurrentUser()
        setupLocationSearch()
    }

    /**
     * Setup location search with debounce
     */
    private fun setupLocationSearch() {
        viewModelScope.launch {
            locationQueryFlow
                .debounce(300) // Wait 300ms after user stops typing
                .filter { it.length >= 2 } // Only search if query is at least 2 chars
                .distinctUntilChanged()
                .collect { query ->
                    searchLocations(query)
                }
        }
    }

    /**
     * Load current user data
     */
    private fun loadCurrentUser() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            when (val result = getCurrentUserUseCase()) {
                is Result.Success -> {
                    val user = result.data
                    // TODO: Load location and maxDistance from preferences
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            firstName = user.firstName,
                            lastName = user.lastName,
                            selectedLocation = null, // Load from preferences
                            maxDistance = 25 // Default or load from preferences
                        )
                    }
                }

                is Result.Error -> {
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
                            error = "Errore durante il caricamento del profilo"
                        )
                    }
                }
            }
        }
    }

    fun onFirstNameChanged(firstName: String) {
        _uiState.update {
            it.copy(
                firstName = firstName,
                firstNameError = null
            )
        }
    }

    fun onLastNameChanged(lastName: String) {
        _uiState.update {
            it.copy(
                lastName = lastName,
                lastNameError = null
            )
        }
    }

    fun onLocationQueryChanged(query: String) {
        _uiState.update {
            it.copy(
                locationQuery = query,
                locationError = null
            )
        }
        locationQueryFlow.value = query
    }

    fun onLocationSelected(location: String) {
        _uiState.update {
            it.copy(
                selectedLocation = location,
                locationQuery = "",
                locationSuggestions = emptyList(),
                locationError = null
            )
        }
    }

    fun onMaxDistanceChanged(distance: Float) {
        _uiState.update {
            it.copy(maxDistance = distance.toInt())
        }
    }

    /**
     * Search locations with autocomplete
     */
    private fun searchLocations(query: String) {
        viewModelScope.launch {
            Timber.d("Searching locations: $query")

            when (val result = searchMunicipalitiesUseCase(query)) {
                is Result.Success -> {
                    val suggestions = result.data.map { it.getFullName() }
                    _uiState.update {
                        it.copy(locationSuggestions = suggestions)
                    }
                }

                is Result.Error -> {
                    Timber.w("Location search error: ${result.message}")
                    _uiState.update {
                        it.copy(locationSuggestions = emptyList())
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(locationSuggestions = emptyList())
                    }
                }
            }
        }
    }

    fun onSaveClick() {
        val currentState = _uiState.value

        // Validate
        var hasError = false

        if (currentState.firstName.isBlank()) {
            _uiState.update { it.copy(firstNameError = "Il nome è obbligatorio") }
            hasError = true
        } else if (currentState.firstName.length < 2) {
            _uiState.update { it.copy(firstNameError = "Il nome deve avere almeno 2 caratteri") }
            hasError = true
        }

        if (currentState.lastName.isBlank()) {
            _uiState.update { it.copy(lastNameError = "Il cognome è obbligatorio") }
            hasError = true
        } else if (currentState.lastName.length < 2) {
            _uiState.update { it.copy(lastNameError = "Il cognome deve avere almeno 2 caratteri") }
            hasError = true
        }

        if (hasError) return

        // Save
        _uiState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            Timber.d("Saving profile changes")

            when (val result = updateProfileUseCase(
                firstName = currentState.firstName.trim(),
                lastName = currentState.lastName.trim(),
                location = currentState.selectedLocation
            )) {
                is Result.Success -> {
                    Timber.i("Profile updated successfully")
                    // TODO: Save location and maxDistance to preferences
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            isSaved = true
                        )
                    }
                }

                is Result.Error -> {
                    Timber.w("Failed to update profile: ${result.message}")
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            error = result.message
                        )
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            error = "Errore durante il salvataggio"
                        )
                    }
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

/**
 * UI State per la schermata di modifica profilo
 */
data class EditProfileUiState(
    val firstName: String = "",
    val lastName: String = "",
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val locationQuery: String = "",
    val locationError: String? = null,
    val selectedLocation: String? = null,
    val locationSuggestions: List<String> = emptyList(),
    val maxDistance: Int = 25,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)