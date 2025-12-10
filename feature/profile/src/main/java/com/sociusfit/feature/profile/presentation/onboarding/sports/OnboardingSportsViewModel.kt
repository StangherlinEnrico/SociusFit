package com.sociusfit.feature.profile.presentation.onboarding.sports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.core.domain.Result
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
 */
class OnboardingSportsViewModel(
    private val getAllSportsUseCase: GetAllSportsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingSportsUiState())
    val uiState: StateFlow<OnboardingSportsUiState> = _uiState.asStateFlow()

    init {
        loadSports()
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
                return@update state.copy(error = "Puoi selezionare massimo 5 sport")
            }

            currentSelections[sportId] = level
            state.copy(
                selectedSports = currentSelections,
                error = null
            )
        }
    }

    fun onSportDeselected(sportId: String) {
        _uiState.update { state ->
            val currentSelections = state.selectedSports.toMutableMap()
            currentSelections.remove(sportId)
            state.copy(selectedSports = currentSelections)
        }
    }

    fun onContinue() {
        val state = _uiState.value

        if (state.selectedSports.isEmpty()) {
            _uiState.update { it.copy(error = "Devi selezionare almeno 1 sport") }
            return
        }

        // Naviga al prossimo step
        // Questo sarà gestito dalla UI
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