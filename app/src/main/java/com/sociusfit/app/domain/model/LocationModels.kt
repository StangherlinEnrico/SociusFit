package com.sociusfit.app.domain.model

/**
 * Domain model representing a region with its municipalities
 */
data class Region(
    val code: String,
    val name: String,
    val municipalities: List<Municipality>
) {
    /**
     * Get municipality by code
     */
    fun getMunicipalityByCode(code: String): Municipality? {
        return municipalities.find { it.code == code }
    }

    /**
     * Search municipalities by name
     */
    fun searchMunicipalities(query: String): List<Municipality> {
        return municipalities.filter {
            it.name.contains(query, ignoreCase = true)
        }
    }

    /**
     * Get municipalities count
     */
    val municipalityCount: Int
        get() = municipalities.size
}

/**
 * Domain model representing a municipality
 */
data class Municipality(
    val code: String,
    val name: String,
    val regionCode: String,
    val regionName: String
) {
    /**
     * Get full location name (e.g., "Agliè (Piemonte)")
     */
    fun getFullName(): String = "$name ($regionName)"
}