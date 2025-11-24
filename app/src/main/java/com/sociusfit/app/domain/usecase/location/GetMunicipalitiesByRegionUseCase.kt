package com.sociusfit.app.domain.usecase.location

import com.sociusfit.app.domain.model.Municipality
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.repository.LocationRepository
import timber.log.Timber

/**
 * Use case for getting municipalities by region
 */
class GetMunicipalitiesByRegionUseCase(
    private val locationRepository: LocationRepository
) {
    /**
     * Get all municipalities for a specific region
     * @param regionCode Region code
     * @return Result with list of municipalities in that region
     */
    suspend operator fun invoke(regionCode: String): Result<List<Municipality>> {
        if (regionCode.isBlank()) {
            Timber.w("Region code is empty")
            return Result.Error("Region code cannot be empty")
        }

        Timber.d("Getting municipalities for region: $regionCode")
        return locationRepository.getMunicipalitiesByRegion(regionCode.trim())
    }
}