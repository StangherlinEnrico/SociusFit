package com.sociusfit.feature.profile.domain.usecase

import com.sociusfit.core.domain.Result
import com.sociusfit.feature.profile.domain.model.Profile
import com.sociusfit.feature.profile.domain.repository.ProfileRepository

/**
 * Use case per ottenere il profilo di un altro utente
 */
class GetProfileByUserIdUseCase(
    private val profileRepository: ProfileRepository
) {

    suspend operator fun invoke(userId: String): Result<Profile> {
        if (userId.isBlank()) {
            return Result.Error(Exception("User ID non valido"))
        }

        return profileRepository.getProfileByUserId(userId)
    }
}