package com.sociusfit.feature.profile.presentation.onboarding.photo

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.core.domain.Result
import com.sociusfit.feature.profile.data.repository.OnboardingRepository
import com.sociusfit.feature.profile.domain.model.Profile
import com.sociusfit.feature.profile.domain.model.ProfileSport
import com.sociusfit.feature.profile.domain.usecase.CreateProfileUseCase
import com.sociusfit.feature.profile.domain.usecase.UploadPhotoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

/**
 * ViewModel per il terzo step dell'onboarding (foto profilo)
 *
 * AGGIORNATO: Non invia latitude/longitude - il backend fa geocoding
 * tramite OpenStreetMap Nominatim quando riceve città nel formato "Comune, Regione"
 */
class OnboardingPhotoViewModel(
    private val uploadPhotoUseCase: UploadPhotoUseCase,
    private val createProfileUseCase: CreateProfileUseCase,
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingPhotoUiState())
    val uiState: StateFlow<OnboardingPhotoUiState> = _uiState.asStateFlow()

    fun onPhotoSelected(uri: Uri) {
        _uiState.update { it.copy(photoUri = uri, error = null) }
    }

    fun onSkip() {
        completeOnboarding(photoUrl = null)
    }

    fun onComplete(photoFile: File?) {
        if (photoFile != null) {
            uploadPhotoAndComplete(photoFile)
        } else {
            completeOnboarding(photoUrl = null)
        }
    }

    private fun uploadPhotoAndComplete(photoFile: File) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true, error = null) }

            when (val result = uploadPhotoUseCase(photoFile)) {
                is Result.Success -> {
                    completeOnboarding(photoUrl = result.data)
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isUploading = false,
                            error = result.exception.message
                        )
                    }
                }
                is Result.Loading -> {
                    _uiState.update { it.copy(isUploading = true) }
                }
            }
        }
    }

    private fun completeOnboarding(photoUrl: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true) }

            // Recupera tutti i dati dal repository
            val onboardingData = onboardingRepository.getCompleteData()

            // Valida che tutti i dati necessari siano presenti
            if (!onboardingData.isComplete) {
                _uiState.update {
                    it.copy(
                        isUploading = false,
                        error = "Dati onboarding incompleti. Torna indietro e completa tutti i campi."
                    )
                }
                return@launch
            }

            // Salva la foto URL nel repository
            if (photoUrl != null) {
                onboardingRepository.savePhotoData(photoUrl)
            }

            // Determina il formato città da inviare al backend
            // Se abbiamo il Municipality completo, usa il formato "Comune, Regione"
            // Altrimenti usa il nome semplice della città
            val cityForBackend = onboardingData.municipality?.formattedForBackend
                ?: onboardingData.city!!

            // Crea il profilo completo
            // NOTA: Inviamo città nel formato "Milano, Lombardia"
            // Il backend farà geocoding automatico con OpenStreetMap Nominatim
            val profile = Profile(
                userId = "", // Sarà popolato dal backend
                firstName = "", // Sarà popolato dal backend
                lastName = "", // Sarà popolato dal backend
                age = onboardingData.age!!,
                city = cityForBackend,  // Formato: "Milano, Lombardia"
                latitude = 0.0,  // Backend farà geocoding e sovrascriverà
                longitude = 0.0,  // Backend farà geocoding e sovrascriverà
                bio = onboardingData.bio,
                maxDistance = onboardingData.maxDistance,
                photoUrl = photoUrl,
                sports = onboardingData.selectedSports.map { (sportId, level) ->
                    ProfileSport(
                        sportId = sportId,
                        sportName = "", // Sarà popolato dal backend
                        level = level
                    )
                },
                gender = ""
            )

            // Invia al backend
            when (val result = createProfileUseCase(profile)) {
                is Result.Success -> {
                    // Cancella dati temporanei
                    onboardingRepository.clear()

                    _uiState.update {
                        it.copy(
                            isUploading = false,
                            isComplete = true,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isUploading = false,
                            error = result.exception.message
                        )
                    }
                }
                is Result.Loading -> {
                    _uiState.update { it.copy(isUploading = true) }
                }
            }
        }
    }
}

data class OnboardingPhotoUiState(
    val photoUri: Uri? = null,
    val isUploading: Boolean = false,
    val isComplete: Boolean = false,
    val error: String? = null
)