package com.sociusfit.feature.profile.domain.usecase

import com.sociusfit.core.domain.Result
import com.sociusfit.feature.profile.domain.model.Profile
import com.sociusfit.feature.profile.domain.repository.ProfileRepository

/**
 * Use case per ottenere il profilo dell'utente corrente
 */
class GetMyProfileUseCase(
    private val profileRepository: ProfileRepository
) {

    suspend operator fun invoke(): Result<Profile> {
        return profileRepository.getMyProfile()
    }
}