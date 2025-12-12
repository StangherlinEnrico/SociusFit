package com.sociusfit.core.domain.model

/**
 * Municipality Model
 *
 * Rappresenta un comune italiano dal file municipalities.json
 *
 * NOTA: Il file JSON NON contiene coordinate geografiche.
 * Il backend si occupa del geocoding tramite OpenStreetMap Nominatim.
 */
data class Municipality(
    val regionCode: String,
    val regionName: String,
    val municipalityCode: String,
    val municipalityName: String
) {
    /**
     * Display name completo per UI: "Milano (Lombardia)"
     */
    val displayName: String
        get() = "$municipalityName ($regionName)"

    /**
     * Nome formattato per invio al backend: "Milano, Lombardia"
     * Il backend estrarrà e farà geocoding con Nominatim usando questo formato
     * Esempio: "Milano, Lombardia" → disambigua da altri comuni con stesso nome
     */
    val formattedForBackend: String
        get() = "$municipalityName, $regionName"
}