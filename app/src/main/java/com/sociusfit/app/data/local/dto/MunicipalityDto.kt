package com.sociusfit.app.data.local.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO for municipality JSON parsing
 * Matches the structure of municipalities.json
 */
data class MunicipalityDto(
    @SerializedName("region_code")
    val regionCode: String,

    @SerializedName("region_name")
    val regionName: String,

    @SerializedName("municipality_code")
    val municipalityCode: String,

    @SerializedName("municipality_name")
    val municipalityName: String
)