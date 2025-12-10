package com.sociusfit.feature.profile.presentation.onboarding.photo

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sociusfit.core.domain.Result
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
 */
class OnboardingPhotoViewModel(
    private val uploadPhotoUseCase: UploadPhotoUseCase,
    private val createProfileUseCase: CreateProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingPhotoUiState())
    val uiState: StateFlow<OnboardingPhotoUiState> = _uiState.asStateFlow()

    fun onPhotoSelected(uri: Uri) {
        _uiState.update { it.copy(photoUri = uri, error = null) }
    }

    fun onSkip(profileData: ProfileData) {
        completeOnboarding(profileData, photoUrl = null)
    }

    fun onComplete(profileData: ProfileData, photoFile: File?) {
        if (photoFile != null) {
            uploadPhotoAndComplete(profileData, photoFile)
        } else {
            completeOnboarding(profileData, photoUrl = null)
        }
    }

    private fun uploadPhotoAndComplete(profileData: ProfileData, photoFile: File) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true, error = null) }

            when (val result = uploadPhotoUseCase(photoFile)) {
                is Result.Success -> {
                    completeOnboarding(profileData, photoUrl = result.data)
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

    private fun completeOnboarding(profileData: ProfileData, photoUrl: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true) }

            val profile = Profile(
                userId = profileData.userId,
                firstName = profileData.firstName,
                lastName = profileData.lastName,
                age = profileData.age,
                city = profileData.city,
                latitude = profileData.latitude,
                longitude = profileData.longitude,
                bio = profileData.bio,
                maxDistance = profileData.maxDistance,
                photoUrl = photoUrl,
                sports = profileData.sports
            )

            when (val result = createProfileUseCase(profile)) {
                is Result.Success -> {
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

/**
 * Data class per passare i dati tra gli step dell'onboarding
 */
data class ProfileData(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val bio: String,
    val maxDistance: Int,
    val sports: List<ProfileSport>
)