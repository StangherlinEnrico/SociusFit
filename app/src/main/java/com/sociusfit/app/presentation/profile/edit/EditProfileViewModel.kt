package com.sociusfit.app.presentation.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.app.domain.model.Municipality
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.usecase.location.GetMunicipalityByCodeUseCase
import com.sociusfit.app.domain.usecase.location.SearchMunicipalitiesUseCase
import com.sociusfit.app.domain.usecase.user.GetCurrentUserUseCase
import com.sociusfit.app.domain.usecase.user.UpdateLocationUseCase
import com.sociusfit.app.domain.usecase.user.UpdateProfileUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * 🔥 FIXED: ViewModel for edit profile screen
 * Separates search field from selected location display
 */
@OptIn(FlowPreview::class)
class EditProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val updateLocationUseCase: UpdateLocationUseCase,
    private val searchMunicipalitiesUseCase: SearchMunicipalitiesUseCase,
    private val getMunicipalityByCodeUseCase: GetMunicipalityByCodeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState

    // Flow for location search with debounce
    private val locationQueryFlow = MutableStateFlow("")

    // Cached municipalities from last search
    private var cachedMunicipalities: List<Municipality> = emptyList()

    init {
        loadUserProfile()
        observeLocationQuery()
    }

    /**
     * Observe location query changes and trigger search with debounce
     */
    private fun observeLocationQuery() {
        viewModelScope.launch {
            locationQueryFlow
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.length >= 2) {
                        searchMunicipalities(query)
                    } else {
                        _uiState.update {
                            it.copy(
                                locationSuggestions = emptyList()
                            )
                        }
                        cachedMunicipalities = emptyList()
                    }
                }
        }
    }

    /**
     * Search municipalities based on query
     */
    private fun searchMunicipalities(query: String) {
        viewModelScope.launch {
            Timber.d("Searching municipalities with query: $query")

            when (val result = searchMunicipalitiesUseCase(query)) {
                is Result.Success -> {
                    cachedMunicipalities = result.data

                    val suggestions = result.data.map { municipality ->
                        "${municipality.name} (${municipality.regionName})"
                    }

                    _uiState.update {
                        it.copy(
                            locationSuggestions = suggestions,
                            locationError = null
                        )
                    }

                    Timber.d("Found ${suggestions.size} municipalities")
                }

                is Result.Error -> {
                    Timber.w("Search failed: ${result.message}")
                    _uiState.update {
                        it.copy(
                            locationSuggestions = emptyList(),
                            locationError = result.message
                        )
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(
                            locationSuggestions = emptyList(),
                            locationError = "Errore durante la ricerca"
                        )
                    }
                }
            }
        }
    }

    /**
     * 🔥 FIXED: Load current user profile
     * Keep search field EMPTY, only show selected location in chip/badge
     */
    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getCurrentUserUseCase()) {
                is Result.Success -> {
                    val user = result.data

                    // If user has a location code, resolve it to display name
                    val displayName = if (user.location != null) {
                        when (val municipalityResult = getMunicipalityByCodeUseCase(user.location)) {
                            is Result.Success -> {
                                val municipality = municipalityResult.data
                                "${municipality.name} (${municipality.regionName})"
                            }
                            else -> user.location // Fallback to code if lookup fails
                        }
                    } else {
                        null
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            firstName = user.firstName,
                            lastName = user.lastName,
                            locationQuery = "",  // 🔥 KEEP SEARCH FIELD EMPTY
                            selectedMunicipalityCode = user.location,
                            selectedMunicipalityName = displayName,
                            maxDistance = user.maxDistance ?: 25
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

    /**
     * 🔥 Handle location selection - store BOTH code and name
     */
    fun onLocationSelected(municipalityDisplayName: String) {
        // Find the municipality from cached results
        val municipality = cachedMunicipalities.find {
            "${it.name} (${it.regionName})" == municipalityDisplayName
        }

        if (municipality != null) {
            Timber.d("Selected municipality: ${municipality.name} (code: ${municipality.code})")

            _uiState.update {
                it.copy(
                    locationQuery = "",  // 🔥 CLEAR SEARCH FIELD after selection
                    selectedMunicipalityCode = municipality.code,
                    selectedMunicipalityName = municipalityDisplayName,
                    locationSuggestions = emptyList()
                )
            }
        } else {
            Timber.w("Municipality not found in cache: $municipalityDisplayName")
        }
    }

    /**
     * 🔥 NEW: Clear selected location
     */
    fun onClearLocation() {
        _uiState.update {
            it.copy(
                locationQuery = "",
                selectedMunicipalityCode = null,
                selectedMunicipalityName = null,
                locationSuggestions = emptyList()
            )
        }
    }

    fun onMaxDistanceChanged(distance: Float) {
        _uiState.update {
            it.copy(maxDistance = distance.toInt())
        }
    }

    /**
     * Save profile with TWO separate API calls
     */
    fun onSaveClick() {
        val currentState = _uiState.value

        // Validate fields
        var hasError = false

        if (currentState.firstName.isBlank()) {
            _uiState.update { it.copy(firstNameError = "Il nome è obbligatorio") }
            hasError = true
        }

        if (currentState.lastName.isBlank()) {
            _uiState.update { it.copy(lastNameError = "Il cognome è obbligatorio") }
            hasError = true
        }

        if (hasError) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            // STEP 1: Update profile (firstName, lastName)
            Timber.d("Updating profile: firstName=${currentState.firstName}, lastName=${currentState.lastName}")

            when (val profileResult = updateProfileUseCase(
                firstName = currentState.firstName.trim(),
                lastName = currentState.lastName.trim()
            )) {
                is Result.Success -> {
                    Timber.i("Profile updated successfully")

                    // STEP 2: Update location with ISTAT CODE
                    Timber.d("Updating location: code=${currentState.selectedMunicipalityCode}, maxDistance=${currentState.maxDistance}")

                    when (val locationResult = updateLocationUseCase(
                        locationCode = currentState.selectedMunicipalityCode,
                        maxDistance = currentState.maxDistance
                    )) {
                        is Result.Success -> {
                            Timber.i("Location updated successfully")

                            _uiState.update {
                                it.copy(
                                    isSaving = false,
                                    isSaved = true
                                )
                            }
                        }

                        is Result.Error -> {
                            Timber.w("Failed to update location: ${locationResult.message}")
                            _uiState.update {
                                it.copy(
                                    isSaving = false,
                                    error = "Profilo aggiornato, ma errore nel salvare la località: ${locationResult.message}"
                                )
                            }
                        }

                        else -> {
                            _uiState.update {
                                it.copy(
                                    isSaving = false,
                                    error = "Profilo aggiornato, ma errore nel salvare la località"
                                )
                            }
                        }
                    }
                }

                is Result.Error -> {
                    Timber.w("Failed to update profile: ${profileResult.message}")
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            error = profileResult.message
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
 * 🔥 UI State
 * locationQuery = campo di ricerca (può essere vuoto anche se c'è una località selezionata)
 * selectedMunicipalityName = località selezionata mostrata nel chip (può essere diversa da locationQuery)
 */
data class EditProfileUiState(
    val firstName: String = "",
    val lastName: String = "",
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val locationQuery: String = "",  // 🔥 Campo di ricerca (può essere vuoto)
    val locationError: String? = null,
    val selectedMunicipalityCode: String? = null,  // 🔥 Codice ISTAT (e.g., "026086")
    val selectedMunicipalityName: String? = null,  // 🔥 Nome visualizzato (e.g., "Treviso (Veneto)")
    val locationSuggestions: List<String> = emptyList(),
    val maxDistance: Int = 25,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)