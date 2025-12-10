package com.sociusfit.feature.profile.presentation.onboarding.bio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel per il primo step dell'onboarding (età, città, bio, distanza massima)
 */
class OnboardingBioViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingBioUiState())
    val uiState: StateFlow<OnboardingBioUiState> = _uiState.asStateFlow()

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

    fun onBioChanged(bio: String) {
        if (bio.length <= 500) {
            _uiState.update { it.copy(bio = bio) }
        }
    }

    fun onMaxDistanceChanged(distance: Int) {
        _uiState.update { it.copy(maxDistance = distance) }
    }

    fun onContinue() {
        val state = _uiState.value

        // Validazione finale
        if (!state.isAgeValid || !state.isCityValid) {
            return
        }

        // Naviga al prossimo step
        // Questo sarà gestito dalla UI attraverso un event o callback
    }
}

data class OnboardingBioUiState(
    val age: String = "",
    val city: String = "",
    val bio: String = "",
    val maxDistance: Int = 25,
    val isAgeValid: Boolean = true,
    val isCityValid: Boolean = true
) {
    val canContinue: Boolean
        get() = age.isNotBlank() &&
                isAgeValid &&
                city.isNotBlank() &&
                isCityValid
}