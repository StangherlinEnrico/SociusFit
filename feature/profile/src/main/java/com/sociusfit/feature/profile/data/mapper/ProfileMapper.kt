package com.sociusfit.feature.profile.data.mapper

import com.sociusfit.core.network.dto.profile.ProfileDto
import com.sociusfit.core.storage.entity.ProfileEntity
import com.sociusfit.feature.profile.domain.model.Profile
import com.sociusfit.feature.profile.domain.model.ProfileSport
import com.sociusfit.feature.profile.domain.model.SportLevel

/**
 * Mapper per conversioni Profile tra layer
 */
class ProfileMapper {

    fun toDomain(dto: ProfileDto): Profile {
        return Profile(
            userId = dto.userId,
            firstName = "", // Non presente nel DTO, deve essere popolato da User
            lastName = "",  // Non presente nel DTO, deve essere popolato da User
            age = 0, // Non presente nel DTO Profile
            city = dto.city ?: "",
            latitude = dto.latitude ?: 0.0,
            longitude = dto.longitude ?: 0.0,
            bio = dto.bio ?: "",
            maxDistance = dto.maxDistance,
            photoUrl = dto.photoUrl,
            sports = dto.sports.map { sportDto ->
                ProfileSport(
                    sportId = sportDto.sportId,
                    sportName = sportDto.sportName,
                    level = SportLevel.valueOf(sportDto.level)
                )
            }
        )
    }

    fun toEntity(dto: ProfileDto): ProfileEntity {
        return ProfileEntity(
            userId = dto.userId,
            age = 0, // Non disponibile nel ProfileDto
            city = dto.city ?: "",
            latitude = dto.latitude ?: 0.0,
            longitude = dto.longitude ?: 0.0,
            bio = dto.bio ?: "",
            maxDistance = dto.maxDistance,
            photoUrl = dto.photoUrl,
            updatedAt = System.currentTimeMillis()
        )
    }

    fun fromEntity(entity: ProfileEntity): Profile {
        // Note: Sports will be loaded separately via ProfileDao relation
        return Profile(
            userId = entity.userId,
            firstName = "", // Will be populated from UserEntity
            lastName = "",  // Will be populated from UserEntity
            age = entity.age,
            city = entity.city,
            latitude = entity.latitude,
            longitude = entity.longitude,
            bio = entity.bio,
            maxDistance = entity.maxDistance,
            photoUrl = entity.photoUrl,
            sports = emptyList() // Will be loaded via join
        )
    }
}