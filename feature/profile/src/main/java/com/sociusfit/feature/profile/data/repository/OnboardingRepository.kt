package com.sociusfit.feature.profile.data.repository

import com.sociusfit.core.domain.model.Municipality
import com.sociusfit.feature.profile.domain.model.SportLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Onboarding Repository
 *
 * Repository temporaneo per salvare i dati dell'onboarding
 * durante il flusso multi-step (Bio → Sports → Photo).
 * I dati vengono mantenuti in memoria e cancellati dopo il completamento.
 *
 * AGGIORNATO: Rimosso latitude/longitude - il backend fa geocoding
 */
class OnboardingRepository {

    private val _onboardingData = MutableStateFlow(OnboardingData())
    val onboardingData: StateFlow<OnboardingData> = _onboardingData.asStateFlow()

    /**
     * Salva dati dello step Bio (Step 1/3)
     */
    fun saveBioData(
        age: Int,
        city: String,
        municipality: Municipality?,
        bio: String,
        maxDistance: Int
    ) {
        _onboardingData.update {
            it.copy(
                age = age,
                city = city,
                municipality = municipality,
                bio = bio,
                maxDistance = maxDistance
            )
        }
    }

    /**
     * Salva dati dello step Sports (Step 2/3)
     */
    fun saveSportsData(selectedSports: Map<String, SportLevel>) {
        _onboardingData.update {
            it.copy(selectedSports = selectedSports)
        }
    }

    /**
     * Salva dati dello step Photo (Step 3/3)
     */
    fun savePhotoData(photoUrl: String?) {
        _onboardingData.update {
            it.copy(photoUrl = photoUrl)
        }
    }

    /**
     * Ottiene i dati completi per la richiesta finale
     */
    fun getCompleteData(): OnboardingData {
        return _onboardingData.value
    }

    /**
     * Cancella tutti i dati temporanei (dopo submit o logout)
     */
    fun clear() {
        _onboardingData.value = OnboardingData()
    }
}

/**
 * Data class per i dati dell'onboarding
 *
 * NOTA: Non include latitude/longitude - il backend fa geocoding automatico
 * tramite OpenStreetMap Nominatim quando riceve il nome della città
 */
data class OnboardingData(
    // Step 1: Bio
    val age: Int? = null,
    val city: String? = null,  // Nome città (es. "Milano", "Roma")
    val municipality: Municipality? = null,  // Oggetto completo con regione
    val bio: String = "",
    val maxDistance: Int = 25,

    // Step 2: Sports
    val selectedSports: Map<String, SportLevel> = emptyMap(),

    // Step 3: Photo
    val photoUrl: String? = null
) {
    /**
     * Verifica se lo step Bio è completo
     */
    val isBioComplete: Boolean
        get() = age != null && age in 18..100 && !city.isNullOrBlank()

    /**
     * Verifica se lo step Sports è completo
     */
    val isSportsComplete: Boolean
        get() = selectedSports.isNotEmpty()

    /**
     * Verifica se l'intero onboarding è completo (photo opzionale)
     */
    val isComplete: Boolean
        get() = isBioComplete && isSportsComplete
}