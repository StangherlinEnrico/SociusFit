package com.sociusfit.feature.profile.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.core.domain.Result
import com.sociusfit.feature.profile.domain.model.Profile
import com.sociusfit.feature.profile.domain.model.SportLevel
import com.sociusfit.feature.profile.domain.usecase.GetAllSportsUseCase
import com.sociusfit.feature.profile.domain.usecase.GetMyProfileUseCase
import com.sociusfit.feature.profile.domain.usecase.UpdateProfileUseCase
import com.sociusfit.feature.profile.domain.usecase.UploadPhotoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

/**
 * ViewModel per modificare il profilo esistente
 */
class EditProfileViewModel(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val uploadPhotoUseCase: UploadPhotoUseCase,
    private val getAllSportsUseCase: GetAllSportsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
        loadSports()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getMyProfileUseCase()) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            profile = result.data,
                            age = result.data.age.toString(),
                            city = result.data.city,
                            bio = result.data.bio,
                            maxDistance = result.data.maxDistance,
                            selectedSports = result.data.sports.associate {
                                it.sportId to it.level
                            },
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

    private fun loadSports() {
        viewModelScope.launch {
            when (val result = getAllSportsUseCase()) {
                is Result.Success -> {
                    _uiState.update { it.copy(availableSports = result.data) }
                }
                is Result.Error -> {
                    // Sports già cachati, ignora errore
                }
                is Result.Loading -> {
                    // Nessuna azione necessaria
                }
            }
        }
    }

    fun onAgeChanged(age: String) {
        val ageInt = age.toIntOrNull()
        _uiState.update {
            it.copy(
                age = age,
                isAgeValid = ageInt != null && ageInt in 18..100,
                hasChanges = true
            )
        }
    }

    fun onCityChanged(city: String) {
        _uiState.update {
            it.copy(
                city = city,
                isCityValid = city.isNotBlank(),
                hasChanges = true
            )
        }
    }

    fun onBioChanged(bio: String) {
        if (bio.length <= 500) {
            _uiState.update { it.copy(bio = bio, hasChanges = true) }
        }
    }

    fun onMaxDistanceChanged(distance: Int) {
        _uiState.update { it.copy(maxDistance = distance, hasChanges = true) }
    }

    fun onSportSelected(sportId: String, level: SportLevel) {
        _uiState.update { state ->
            val currentSelections = state.selectedSports.toMutableMap()

            if (currentSelections.size >= 5 && sportId !in currentSelections) {
                return@update state.copy(error = "Puoi selezionare massimo 5 sport")
            }

            currentSelections[sportId] = level
            state.copy(
                selectedSports = currentSelections,
                hasChanges = true,
                error = null
            )
        }
    }

    fun onSportDeselected(sportId: String) {
        _uiState.update { state ->
            val currentSelections = state.selectedSports.toMutableMap()
            currentSelections.remove(sportId)
            state.copy(selectedSports = currentSelections, hasChanges = true)
        }
    }

    fun onPhotoSelected(photoFile: File) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploadingPhoto = true, error = null) }

            when (val result = uploadPhotoUseCase(photoFile)) {
                is Result.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            profile = state.profile?.copy(photoUrl = result.data),
                            isUploadingPhoto = false,
                            hasChanges = true
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isUploadingPhoto = false,
                            error = result.exception.message
                        )
                    }
                }
                is Result.Loading -> {
                    _uiState.update { it.copy(isUploadingPhoto = true) }
                }
            }
        }
    }

    fun onSave() {
        val state = _uiState.value
        val profile = state.profile ?: return

        if (!state.isAgeValid || !state.isCityValid) {
            return
        }

        if (state.selectedSports.isEmpty()) {
            _uiState.update { it.copy(error = "Devi selezionare almeno 1 sport") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            val updatedProfile = profile.copy(
                age = state.age.toInt(),
                city = state.city,
                bio = state.bio,
                maxDistance = state.maxDistance,
                sports = state.availableSports
                    .filter { it.id in state.selectedSports.keys }
                    .map { sport ->
                        com.sociusfit.feature.profile.domain.model.ProfileSport(
                            sportId = sport.id,
                            sportName = sport.name,
                            level = state.selectedSports[sport.id]!!
                        )
                    }
            )

            when (val result = updateProfileUseCase(updatedProfile)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            profile = result.data,
                            isSaving = false,
                            hasChanges = false,
                            isSaved = true
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            error = result.exception.message
                        )
                    }
                }
                is Result.Loading -> {
                    _uiState.update { it.copy(isSaving = true) }
                }
            }
        }
    }
}

data class EditProfileUiState(
    val profile: Profile? = null,
    val availableSports: List<com.sociusfit.feature.profile.domain.model.Sport> = emptyList(),
    val age: String = "",
    val city: String = "",
    val bio: String = "",
    val maxDistance: Int = 25,
    val selectedSports: Map<String, SportLevel> = emptyMap(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isUploadingPhoto: Boolean = false,
    val hasChanges: Boolean = false,
    val isSaved: Boolean = false,
    val isAgeValid: Boolean = true,
    val isCityValid: Boolean = true,
    val error: String? = null
) {
    val canSave: Boolean
        get() = !isSaving &&
                !isLoading &&
                hasChanges &&
                isAgeValid &&
                isCityValid &&
                selectedSports.isNotEmpty()
}