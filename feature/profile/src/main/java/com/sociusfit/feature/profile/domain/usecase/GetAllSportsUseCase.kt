package com.sociusfit.feature.profile.domain.usecase

import com.sociusfit.core.domain.Result
import com.sociusfit.feature.profile.domain.model.Sport
import com.sociusfit.feature.profile.domain.repository.SportsRepository

/**
 * Use case per ottenere tutti gli sport disponibili
 */
class GetAllSportsUseCase(
    private val sportsRepository: SportsRepository
) {

    suspend operator fun invoke(): Result<List<Sport>> {
        return sportsRepository.getAllSports()
    }
}