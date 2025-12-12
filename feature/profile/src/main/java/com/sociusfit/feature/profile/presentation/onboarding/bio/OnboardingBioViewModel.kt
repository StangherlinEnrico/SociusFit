package com.sociusfit.feature.profile.presentation.onboarding.bio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.core.domain.model.Municipality
import com.sociusfit.core.storage.provider.MunicipalityProvider
import com.sociusfit.feature.profile.data.repository.OnboardingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel per il primo step dell'onboarding (età, città, bio, distanza massima)
 *
 * AGGIORNATO: Salva i dati in OnboardingRepository
 */
class OnboardingBioViewModel(
    private val municipalityProvider: MunicipalityProvider,
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingBioUiState())
    val uiState: StateFlow<OnboardingBioUiState> = _uiState.asStateFlow()

    init {
        // Carica i comuni all'avvio
        viewModelScope.launch {
            val municipalities = municipalityProvider.loadMunicipalities()
            _uiState.update { it.copy(municipalities = municipalities) }
        }

        // Ripristina dati salvati se presenti (es. tornando indietro)
        viewModelScope.launch {
            val savedData = onboardingRepository.onboardingData.value
            if (savedData.age != null) {
                _uiState.update {
                    it.copy(
                        age = savedData.age.toString(),
                        city = savedData.city ?: "",
                        selectedMunicipality = savedData.municipality,
                        bio = savedData.bio,
                        maxDistance = savedData.maxDistance,
                        isAgeValid = savedData.age in 18..100,
                        isCityValid = !savedData.city.isNullOrBlank()
                    )
                }
            }
        }
    }

    fun onAgeChanged(age: String) {
        val ageInt = age.toIntOrNull()
        _uiState.update {
            it.copy(
                age = age,
                isAgeValid = ageInt != null && ageInt in 18..100
            )
        }
    }

    fun onCityChanged(city: String) {
        _uiState.update {
            it.copy(
                city = city,
                isCityValid = city.isNotBlank()
            )
        }
    }

    fun onCitySelected(municipality: Municipality) {
        _uiState.update {
            it.copy(
                city = municipality.municipalityName,
                selectedMunicipality = municipality,
                isCityValid = true
            )
        }
    }

    fun onBioChanged(bio: String) {
        if (bio.length <= 500) {
            _uiState.update { it.copy(bio = bio) }
        }
    }

    fun onMaxDistanceChanged(distance: Int) {
        _uiState.update { it.copy(maxDistance = distance) }
    }

    /**
     * Salva i dati nel repository prima di continuare
     */
    fun saveAndContinue(): Boolean {
        val state = _uiState.value

        // Validazione finale
        if (!state.isAgeValid || !state.isCityValid) {
            return false
        }

        // Salva nel repository
        onboardingRepository.saveBioData(
            age = state.age.toInt(),
            city = state.city,
            municipality = state.selectedMunicipality,
            bio = state.bio,
            maxDistance = state.maxDistance
        )

        return true
    }
}

data class OnboardingBioUiState(
    val age: String = "",
    val city: String = "",
    val bio: String = "",
    val maxDistance: Int = 25,
    val isAgeValid: Boolean = true,
    val isCityValid: Boolean = true,
    val municipalities: List<Municipality> = emptyList(),
    val selectedMunicipality: Municipality? = null
) {
    val canContinue: Boolean
        get() = age.isNotBlank() &&
                isAgeValid &&
                city.isNotBlank() &&
                isCityValid
}