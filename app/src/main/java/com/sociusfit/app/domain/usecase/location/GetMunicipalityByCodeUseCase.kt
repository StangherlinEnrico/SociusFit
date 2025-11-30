package com.sociusfit.app.domain.usecase.location

import com.sociusfit.app.domain.model.Municipality
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.repository.LocationRepository
import timber.log.Timber

/**
 * Use case for getting municipality by ISTAT code
 */
class GetMunicipalityByCodeUseCase(
    private val locationRepository: LocationRepository
) {
    /**
     * Get municipality by code
     * @param code Municipality ISTAT code (e.g., "026086")
     * @return Result with Municipality or error
     */
    suspend operator fun invoke(code: String): Result<Municipality> {
        if (code.isBlank()) {
            Timber.w("Get municipality failed: Code is empty")
            return Result.Error("Municipality code cannot be empty")
        }

        if (!code.matches(Regex("^\\d{6}$"))) {
            Timber.w("Get municipality failed: Invalid code format: $code")
            return Result.Error("Invalid municipality code format")
        }

        return locationRepository.getMunicipalityByCode(code)
    }
}