package com.sociusfit.app.domain.repository

import com.sociusfit.app.domain.model.Municipality
import com.sociusfit.app.domain.model.Region
import com.sociusfit.app.domain.model.Result

/**
 * Repository interface for location data (regions and municipalities)
 */
interface LocationRepository {

    /**
     * Get all regions with their municipalities
     * @return Result with list of regions or error
     */
    suspend fun getAllRegions(): Result<List<Region>>

    /**
     * Get all municipalities (flat list)
     * @return Result with list of municipalities or error
     */
    suspend fun getAllMunicipalities(): Result<List<Municipality>>

    /**
     * Get region by code
     * @param regionCode Region code
     * @return Result with region or error
     */
    suspend fun getRegionByCode(regionCode: String): Result<Region>

    /**
     * Get municipalities by region code
     * @param regionCode Region code
     * @return Result with list of municipalities or error
     */
    suspend fun getMunicipalitiesByRegion(regionCode: String): Result<List<Municipality>>

    /**
     * Search municipalities by name
     * @param query Search query
     * @return Result with list of matching municipalities or error
     */
    suspend fun searchMunicipalities(query: String): Result<List<Municipality>>

    /**
     * Get municipality by code
     * @param municipalityCode Municipality code
     * @return Result with municipality or error
     */
    suspend fun getMunicipalityByCode(municipalityCode: String): Result<Municipality>
}