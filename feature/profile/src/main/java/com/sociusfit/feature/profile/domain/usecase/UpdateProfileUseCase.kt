package com.sociusfit.feature.profile.domain.usecase

import com.sociusfit.core.domain.Result
import com.sociusfit.feature.profile.domain.model.Profile
import com.sociusfit.feature.profile.domain.repository.ProfileRepository

/**
 * Use case per aggiornare il profilo esistente
 */
class UpdateProfileUseCase(
    private val profileRepository: ProfileRepository
) {

    suspend operator fun invoke(profile: Profile): Result<Profile> {
        // Validazione
        if (profile.age < Profile.MIN_AGE || profile.age > Profile.MAX_AGE) {
            return Result.Error(Exception("L'età deve essere tra ${Profile.MIN_AGE} e ${Profile.MAX_AGE}"))
        }

        if (profile.city.isBlank()) {
            return Result.Error(Exception("La città è obbligatoria"))
        }

        if (profile.bio.length > Profile.MAX_BIO_LENGTH) {
            return Result.Error(Exception("La bio non può superare ${Profile.MAX_BIO_LENGTH} caratteri"))
        }

        if (profile.maxDistance < Profile.MIN_DISTANCE || profile.maxDistance > Profile.MAX_DISTANCE) {
            return Result.Error(Exception("La distanza massima deve essere tra ${Profile.MIN_DISTANCE} e ${Profile.MAX_DISTANCE} km"))
        }

        if (profile.sports.size < Profile.MIN_SPORTS) {
            return Result.Error(Exception("Devi selezionare almeno ${Profile.MIN_SPORTS} sport"))
        }

        if (profile.sports.size > Profile.MAX_SPORTS) {
            return Result.Error(Exception("Puoi selezionare massimo ${Profile.MAX_SPORTS} sport"))
        }

        return profileRepository.updateProfile(profile)
    }
}