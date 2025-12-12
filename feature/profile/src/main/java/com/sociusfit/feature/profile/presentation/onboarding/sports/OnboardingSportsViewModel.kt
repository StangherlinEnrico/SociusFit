package com.sociusfit.feature.profile.presentation.onboarding.sports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.core.domain.Result
import com.sociusfit.feature.profile.data.repository.OnboardingRepository
import com.sociusfit.feature.profile.domain.model.Sport
import com.sociusfit.feature.profile.domain.model.SportLevel
import com.sociusfit.feature.profile.domain.usecase.GetAllSportsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel per il secondo step dell'onboarding (selezione sport)
 *
 * AGGIORNATO: Salva i dati in OnboardingRepository
 */
class OnboardingSportsViewModel(
    private val getAllSportsUseCase: GetAllSportsUseCase,
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingSportsUiState())
    val uiState: StateFlow<OnboardingSportsUiState> = _uiState.asStateFlow()

    init {
        loadSports()

        // Ripristina sport selezionati se presenti (tornando indietro)
        viewModelScope.launch {
            val savedData = onboardingRepository.onboardingData.value
            if (savedData.selectedSports.isNotEmpty()) {
                _uiState.update {
                    it.copy(selectedSports = savedData.selectedSports)
                }
            }
        }
    }

    private fun loadSports() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getAllSportsUseCase()) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            availableSports = result.data,
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

    fun onSportSelected(sportId: String, level: SportLevel) {
        _uiState.update { state ->
            val currentSelections = state.selectedSports.toMutableMap()

            // Max 5 sport
            if (currentSelections.size >= 5 && sportId !in currentSelections) {
                return@update state
            }

            currentSelections[sportId] = level
            state.copy(selectedSports = currentSelections)
        }
    }

    fun onSportDeselected(sportId: String) {
        _uiState.update { state ->
            val currentSelections = state.selectedSports.toMutableMap()
            currentSelections.remove(sportId)
            state.copy(selectedSports = currentSelections)
        }
    }

    /**
     * Salva i dati nel repository prima di continuare
     */
    fun saveAndContinue(): Boolean {
        val state = _uiState.value

        // Validazione: almeno 1 sport
        if (state.selectedSports.isEmpty()) {
            return false
        }

        // Salva nel repository
        onboardingRepository.saveSportsData(state.selectedSports)

        return true
    }
}

data class OnboardingSportsUiState(
    val availableSports: List<Sport> = emptyList(),
    val selectedSports: Map<String, SportLevel> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val canContinue: Boolean
        get() = selectedSports.isNotEmpty() && selectedSports.size <= 5
}