package com.sociusfit.app.domain.usecase.location

import com.sociusfit.app.domain.model.Region
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.repository.LocationRepository
import timber.log.Timber

/**
 * Use case for getting all regions
 */
class GetAllRegionsUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(): Result<List<Region>> {
        Timber.d("Getting all regions")
        return locationRepository.getAllRegions()
    }
}