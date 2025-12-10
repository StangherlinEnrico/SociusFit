package com.sociusfit.feature.profile.data.repository

import com.sociusfit.core.domain.Result
import com.sociusfit.core.network.api.ProfileApiService
import com.sociusfit.core.network.dto.profile.SportLevelRequest
import com.sociusfit.core.network.dto.profile.UpdateProfileRequest
import com.sociusfit.core.storage.dao.ProfileDao
import com.sociusfit.feature.profile.data.mapper.ProfileMapper
import com.sociusfit.feature.profile.domain.model.Profile
import com.sociusfit.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File

/**
 * Implementazione repository profilo
 * Gestisce cache locale e sincronizzazione con backend
 */
class ProfileRepositoryImpl(
    private val apiService: ProfileApiService,
    private val profileDao: ProfileDao,
    private val mapper: ProfileMapper
) : ProfileRepository {

    override suspend fun createProfile(profile: Profile): Result<Profile> {
        return try {
            // Backend usa updateProfile sia per create che update
            val request = UpdateProfileRequest(
                bio = profile.bio,
                city = profile.city,
                maxDistance = profile.maxDistance,
                sports = profile.sports.map {
                    SportLevelRequest(
                        sportId = it.sportId,
                        level = it.level.name
                    )
                }
            )

            val response = apiService.updateProfile(request)

            // Cache in Room
            val entity = mapper.toEntity(response)
            profileDao.insertProfile(entity)

            val createdProfile = mapper.toDomain(response)
            Result.Success(createdProfile)
        } catch (e: Exception) {
            Timber.e(e, "Error creating profile")
            Result.Error(Exception("Failed to create profile", e))
        }
    }

    override suspend fun updateProfile(profile: Profile): Result<Profile> {
        return try {
            val request = UpdateProfileRequest(
                bio = profile.bio,
                city = profile.city,
                maxDistance = profile.maxDistance,
                sports = profile.sports.map {
                    SportLevelRequest(
                        sportId = it.sportId,
                        level = it.level.name
                    )
                }
            )

            val response = apiService.updateProfile(request)

            // Update cache
            val entity = mapper.toEntity(response)
            profileDao.updateProfile(entity)

            val updatedProfile = mapper.toDomain(response)
            Result.Success(updatedProfile)
        } catch (e: Exception) {
            Timber.e(e, "Error updating profile")
            Result.Error(Exception("Failed to update profile", e))
        }
    }

    override suspend fun getMyProfile(): Result<Profile> {
        return try {
            val response = apiService.getMyProfile()

            // Update cache
            val entity = mapper.toEntity(response)
            profileDao.insertProfile(entity)

            val profile = mapper.toDomain(response)
            Result.Success(profile)
        } catch (e: Exception) {
            Timber.e(e, "Error getting my profile")

            // Try to load from cache
            val cachedEntity = profileDao.getMyProfile()
            if (cachedEntity != null) {
                val cachedProfile = mapper.fromEntity(cachedEntity)
                return Result.Success(cachedProfile)
            }

            Result.Error(Exception("Failed to get profile", e))
        }
    }

    override fun observeMyProfile(): Flow<Profile?> {
        return profileDao.observeMyProfile()
            .map { entity ->
                entity?.let { mapper.fromEntity(it) }
            }
    }

    override suspend fun getProfileByUserId(userId: String): Result<Profile> {
        return try {
            val response = apiService.getProfile(userId)
            val profile = mapper.toDomain(response)
            Result.Success(profile)
        } catch (e: Exception) {
            Timber.e(e, "Error getting profile for user $userId")
            Result.Error(Exception("Failed to get profile", e))
        }
    }

    override suspend fun uploadPhoto(photoFile: File): Result<String> {
        return try {
            // Prepare multipart request
            val requestBody = photoFile.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("photo", photoFile.name, requestBody)

            val response = apiService.uploadPhoto(part)
            Result.Success(response.photoUrl)
        } catch (e: Exception) {
            Timber.e(e, "Error uploading photo")
            Result.Error(Exception("Failed to upload photo", e))
        }
    }
}