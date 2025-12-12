package com.sociusfit.feature.profile.data.repository

import com.sociusfit.core.domain.Result
import com.sociusfit.core.network.api.ProfileApiService
import com.sociusfit.core.network.dto.profile.CreateProfileRequest
import com.sociusfit.core.network.dto.profile.SportLevelRequest
import com.sociusfit.core.network.dto.profile.UpdateProfileRequest
import com.sociusfit.core.storage.dao.ProfileDao
import com.sociusfit.feature.profile.data.mapper.ProfileMapper
import com.sociusfit.feature.profile.domain.model.Profile
import com.sociusfit.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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

    /**
     * Crea un nuovo profilo (chiamato durante onboarding)
     * Backend: POST /api/profiles
     */
    override suspend fun createProfile(profile: Profile): Result<Profile> {
        return withContext(Dispatchers.IO) {
            try {
                // Converti Profile domain model in CreateProfileRequest DTO
                val request = CreateProfileRequest(
                    age = profile.age,
                    gender = profile.gender, // IMPORTANTE: Richiesto dal backend!
                    city = profile.city,
                    bio = profile.bio,
                    maxDistance = profile.maxDistance,
                    sports = profile.sports.map {
                        SportLevelRequest(
                            sportId = it.sportId,
                            level = it.level.toBackendValue() // Converti enum → numero (1,2,3)
                        )
                    }
                )

                // Chiama POST /api/profiles (creazione nuova)
                val response = apiService.createProfile(request)

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
    }

    /**
     * Aggiorna profilo esistente
     * Backend: PUT /api/profiles (NO /me!)
     */
    override suspend fun updateProfile(profile: Profile): Result<Profile> {
        return withContext(Dispatchers.IO) {
            try {
                // Converti Profile domain model in UpdateProfileRequest DTO
                val request = UpdateProfileRequest(
                    age = profile.age,
                    gender = profile.gender, // IMPORTANTE: Richiesto dal backend!
                    bio = profile.bio,
                    city = profile.city,
                    maxDistance = profile.maxDistance,
                    sports = profile.sports.map {
                        SportLevelRequest(
                            sportId = it.sportId,
                            level = it.level.toBackendValue() // Converti enum → numero (1,2,3)
                        )
                    }
                )

                // Chiama PUT /api/profiles (update esistente, NO /me!)
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
    }

    /**
     * Ottieni profilo dell'utente corrente
     * Backend: GET /api/profiles/me
     */
    override suspend fun getMyProfile(): Result<Profile> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMyProfile()

                // Update cache
                val entity = mapper.toEntity(response)
                profileDao.insertProfile(entity)

                val profile = mapper.toDomain(response)
                Result.Success(profile)
            } catch (e: Exception) {
                Timber.e(e, "Error getting my profile")

                // Try to load from cache on network error
                val cachedEntity = profileDao.getMyProfile()
                if (cachedEntity != null) {
                    val cachedProfile = mapper.fromEntity(cachedEntity)
                    return@withContext Result.Success(cachedProfile)
                }

                Result.Error(Exception("Failed to get profile", e))
            }
        }
    }

    /**
     * Osserva i cambiamenti del profilo corrente (reactive)
     */
    override fun observeMyProfile(): Flow<Profile?> {
        return profileDao.observeMyProfile()
            .map { entity ->
                entity?.let { mapper.fromEntity(it) }
            }
    }

    /**
     * Ottieni profilo di un altro utente
     * Backend: GET /api/profiles/user/{userId}
     */
    override suspend fun getProfileByUserId(userId: String): Result<Profile> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getProfile(userId)
                val profile = mapper.toDomain(response)
                Result.Success(profile)
            } catch (e: Exception) {
                Timber.e(e, "Error getting profile for user $userId")
                Result.Error(Exception("Failed to get profile", e))
            }
        }
    }

    /**
     * Carica foto profilo
     * Backend: POST /api/profiles/photo (NO /me!)
     * L'utente viene identificato dal JWT token nell'Authorization header
     */
    override suspend fun uploadPhoto(photoFile: File): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                // Prepare multipart request
                val requestBody = photoFile.asRequestBody("image/*".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData(
                    name = "photo",
                    filename = photoFile.name,
                    body = requestBody
                )

                val response = apiService.uploadPhoto(part)
                Result.Success(response.photoUrl)
            } catch (e: Exception) {
                Timber.e(e, "Error uploading photo")
                Result.Error(Exception("Failed to upload photo", e))
            }
        }
    }
}

/**
 * Helper per convertire SportLevel domain enum in numero backend
 */
fun com.sociusfit.feature.profile.domain.model.SportLevel.toBackendValue(): Int {
    return when (this) {
        com.sociusfit.feature.profile.domain.model.SportLevel.Beginner -> 1
        com.sociusfit.feature.profile.domain.model.SportLevel.Intermediate -> 2
        com.sociusfit.feature.profile.domain.model.SportLevel.Advanced -> 3
        com.sociusfit.feature.profile.domain.model.SportLevel.Expert -> 3 // Expert → Advanced (backend non ha Expert)
    }
}