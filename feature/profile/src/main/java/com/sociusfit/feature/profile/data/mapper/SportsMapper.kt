package com.sociusfit.feature.profile.data.mapper

import com.sociusfit.core.network.dto.sports.SportDto
import com.sociusfit.core.storage.entity.SportEntity
import com.sociusfit.feature.profile.domain.model.Sport

/**
 * Mapper per conversioni Sport tra layer
 */
class SportsMapper {

    fun toDomain(dto: SportDto): Sport {
        return Sport(
            id = dto.id,
            name = dto.name
        )
    }

    fun toEntity(dto: SportDto): SportEntity {
        return SportEntity(
            id = dto.id,
            name = dto.name,
            createdAt = System.currentTimeMillis()
        )
    }

    fun fromEntity(entity: SportEntity): Sport {
        return Sport(
            id = entity.id,
            name = entity.name
        )
    }
}