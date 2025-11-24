package com.sociusfit.app.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sociusfit.app.R
import com.sociusfit.app.data.local.dto.MunicipalityDto
import com.sociusfit.app.data.local.mapper.LocationMapper
import com.sociusfit.app.domain.model.Municipality
import com.sociusfit.app.domain.model.Region
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.repository.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Implementation of LocationRepository
 * Loads data from municipalities.json in res/raw
 */
class LocationRepositoryImpl(
    private val context: Context,
    private val gson: Gson
) : LocationRepository {

    // Cache per evitare di parsare il JSON ogni volta
    private var cachedRegions: List<Region>? = null
    private var cachedMunicipalities: List<Municipality>? = null

    /**
     * Load and parse municipalities.json
     */
    private suspend fun loadMunicipalitiesFromJson(): List<MunicipalityDto> = withContext(Dispatchers.IO) {
        try {
            Timber.d("Loading municipalities from JSON")

            val jsonString = context.resources
                .openRawResource(R.raw.municipalities)
                .bufferedReader()
                .use { it.readText() }

            val type = object : TypeToken<List<MunicipalityDto>>() {}.type
            val dtoList: List<MunicipalityDto> = gson.fromJson(jsonString, type)

            Timber.i("Loaded ${dtoList.size} municipalities from JSON")
            dtoList
        } catch (e: Exception) {
            Timber.e(e, "Error loading municipalities from JSON")
            emptyList()
        }
    }

    /**
     * Initialize cache if needed
     */
    private suspend fun ensureCacheInitialized() {
        if (cachedRegions == null || cachedMunicipalities == null) {
            val dtoList = loadMunicipalitiesFromJson()
            cachedRegions = LocationMapper.toRegionList(dtoList)
            cachedMunicipalities = LocationMapper.toMunicipalityList(dtoList)
            Timber.d("Cache initialized: ${cachedRegions?.size} regions, ${cachedMunicipalities?.size} municipalities")
        }
    }

    override suspend fun getAllRegions(): Result<List<Region>> {
        return try {
            ensureCacheInitialized()
            Result.Success(cachedRegions ?: emptyList())
        } catch (e: Exception) {
            Timber.e(e, "Error getting all regions")
            Result.Error("Failed to load regions: ${e.message}")
        }
    }

    override suspend fun getAllMunicipalities(): Result<List<Municipality>> {
        return try {
            ensureCacheInitialized()
            Result.Success(cachedMunicipalities ?: emptyList())
        } catch (e: Exception) {
            Timber.e(e, "Error getting all municipalities")
            Result.Error("Failed to load municipalities: ${e.message}")
        }
    }

    override suspend fun getRegionByCode(regionCode: String): Result<Region> {
        return try {
            ensureCacheInitialized()
            val region = cachedRegions?.find { it.code == regionCode }
            if (region != null) {
                Result.Success(region)
            } else {
                Timber.w("Region not found: $regionCode")
                Result.Error("Region not found")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error getting region by code: $regionCode")
            Result.Error("Failed to get region: ${e.message}")
        }
    }

    override suspend fun getMunicipalitiesByRegion(regionCode: String): Result<List<Municipality>> {
        return try {
            ensureCacheInitialized()
            val municipalities = cachedMunicipalities?.filter { it.regionCode == regionCode } ?: emptyList()
            Result.Success(municipalities)
        } catch (e: Exception) {
            Timber.e(e, "Error getting municipalities by region: $regionCode")
            Result.Error("Failed to get municipalities: ${e.message}")
        }
    }

    override suspend fun searchMunicipalities(query: String): Result<List<Municipality>> {
        return try {
            if (query.isBlank()) {
                return Result.Success(emptyList())
            }

            ensureCacheInitialized()
            val results = cachedMunicipalities?.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.regionName.contains(query, ignoreCase = true)
            } ?: emptyList()

            Timber.d("Search '$query' found ${results.size} municipalities")
            Result.Success(results)
        } catch (e: Exception) {
            Timber.e(e, "Error searching municipalities: $query")
            Result.Error("Search failed: ${e.message}")
        }
    }

    override suspend fun getMunicipalityByCode(municipalityCode: String): Result<Municipality> {
        return try {
            ensureCacheInitialized()
            val municipality = cachedMunicipalities?.find { it.code == municipalityCode }
            if (municipality != null) {
                Result.Success(municipality)
            } else {
                Timber.w("Municipality not found: $municipalityCode")
                Result.Error("Municipality not found")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error getting municipality by code: $municipalityCode")
            Result.Error("Failed to get municipality: ${e.message}")
        }
    }

    /**
     * Clear cache (useful for testing or memory management)
     */
    fun clearCache() {
        cachedRegions = null
        cachedMunicipalities = null
        Timber.d("Location cache cleared")
    }
}