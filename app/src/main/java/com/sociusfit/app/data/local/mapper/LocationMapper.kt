package com.sociusfit.app.data.local.mapper

import com.sociusfit.app.data.local.dto.MunicipalityDto
import com.sociusfit.app.domain.model.Municipality
import com.sociusfit.app.domain.model.Region

/**
 * Mapper for converting MunicipalityDto list to Region list
 */
object LocationMapper {

    /**
     * Convert list of MunicipalityDto to list of Region with their municipalities
     * Groups municipalities by region
     */
    fun toRegionList(dtoList: List<MunicipalityDto>): List<Region> {
        return dtoList
            .groupBy { it.regionCode to it.regionName }
            .map { (regionInfo, municipalities) ->
                Region(
                    code = regionInfo.first,
                    name = regionInfo.second,
                    municipalities = municipalities.map { dto ->
                        Municipality(
                            code = dto.municipalityCode,
                            name = dto.municipalityName,
                            regionCode = dto.regionCode,
                            regionName = dto.regionName
                        )
                    }.sortedBy { it.name } // Sort municipalities alphabetically
                )
            }
            .sortedBy { it.name } // Sort regions alphabetically
    }

    /**
     * Convert list of MunicipalityDto to flat list of Municipality
     */
    fun toMunicipalityList(dtoList: List<MunicipalityDto>): List<Municipality> {
        return dtoList.map { dto ->
            Municipality(
                code = dto.municipalityCode,
                name = dto.municipalityName,
                regionCode = dto.regionCode,
                regionName = dto.regionName
            )
        }.sortedBy { it.name }
    }

    /**
     * Get all unique regions from DTO list
     */
    fun getRegions(dtoList: List<MunicipalityDto>): List<Pair<String, String>> {
        return dtoList
            .map { it.regionCode to it.regionName }
            .distinct()
            .sortedBy { it.second }
    }
}