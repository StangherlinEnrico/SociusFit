package com.sociusfit.app.domain.usecase.location

import com.sociusfit.app.domain.model.Municipality
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.repository.LocationRepository
import timber.log.Timber

/**
 * Use case for searching municipalities
 */
class SearchMunicipalitiesUseCase(
    private val locationRepository: LocationRepository
) {
    /**
     * Search municipalities by name
     * @param query Search query (minimum 2 characters)
     * @return Result with list of matching municipalities
     */
    suspend operator fun invoke(query: String): Result<List<Municipality>> {
        // Validate query
        if (query.isBlank()) {
            Timber.w("Search query is empty")
            return Result.Success(emptyList())
        }

        if (query.length < 2) {
            Timber.w("Search query too short: $query")
            return Result.Error("Inserisci almeno 2 caratteri per la ricerca")
        }

        Timber.d("Searching municipalities with query: $query")
        return locationRepository.searchMunicipalities(query.trim())
    }
}