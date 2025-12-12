package com.sociusfit.core.storage.provider

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.sociusfit.core.domain.model.Municipality
import timber.log.Timber

/**
 * Municipality Provider
 *
 * Fornisce l'accesso ai comuni italiani dal file municipalities.json
 * Posizionato in :core:storage perché gestisce dati locali
 */
class MunicipalityProvider(private val context: Context) {

    companion object {
        private const val TAG = "MunicipalityProvider"
        private const val MUNICIPALITIES_FILE = "municipalities.json"
    }

    private var cachedMunicipalities: List<Municipality>? = null

    /**
     * DTO per deserializzazione JSON
     */
    private data class MunicipalityDto(
        @SerializedName("region_code")
        val regionCode: String,
        @SerializedName("region_name")
        val regionName: String,
        @SerializedName("municipality_code")
        val municipalityCode: String,
        @SerializedName("municipality_name")
        val municipalityName: String
    )

    /**
     * Carica tutti i comuni dal file JSON
     */
    fun loadMunicipalities(): List<Municipality> {
        // Usa cache se disponibile
        cachedMunicipalities?.let { return it }

        return try {
            Timber.tag(TAG).d("Loading municipalities from raw resource...")

            val json = context.resources
                .openRawResource(
                    context.resources.getIdentifier(
                        MUNICIPALITIES_FILE.substringBefore("."),
                        "raw",
                        context.packageName
                    )
                )
                .bufferedReader()
                .use { it.readText() }

            val gson = Gson()
            val type = object : TypeToken<List<MunicipalityDto>>() {}.type
            val dtos: List<MunicipalityDto> = gson.fromJson(json, type)

            val municipalities = dtos.map { dto ->
                Municipality(
                    regionCode = dto.regionCode,
                    regionName = dto.regionName,
                    municipalityCode = dto.municipalityCode,
                    municipalityName = dto.municipalityName
                )
            }

            cachedMunicipalities = municipalities
            Timber.tag(TAG).d("✓ Loaded ${municipalities.size} municipalities")

            municipalities
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "✗ Failed to load municipalities")
            emptyList()
        }
    }

    /**
     * Cerca comuni per nome (case-insensitive, partial match)
     */
    fun searchMunicipalities(query: String): List<Municipality> {
        if (query.length < 2) return emptyList()

        val municipalities = loadMunicipalities()
        val normalizedQuery = query.trim().lowercase()

        return municipalities.filter {
            it.municipalityName.lowercase().contains(normalizedQuery) ||
                    it.regionName.lowercase().contains(normalizedQuery)
        }.take(10) // Limita a 10 risultati per performance
    }

    /**
     * Ottiene un comune per nome esatto
     */
    fun getMunicipalityByName(name: String): Municipality? {
        val municipalities = loadMunicipalities()
        return municipalities.firstOrNull {
            it.municipalityName.equals(name, ignoreCase = true)
        }
    }

    /**
     * Ottiene tutti i comuni di una regione
     */
    fun getMunicipalitiesByRegion(regionName: String): List<Municipality> {
        val municipalities = loadMunicipalities()
        return municipalities.filter {
            it.regionName.equals(regionName, ignoreCase = true)
        }
    }
}