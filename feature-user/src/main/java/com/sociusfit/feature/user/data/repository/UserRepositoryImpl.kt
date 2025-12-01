package com.sociusfit.feature.user.data.repository

import com.sociusfit.feature.user.data.mapper.toDomain
import com.sociusfit.feature.user.data.remote.api.UserApiService
import com.sociusfit.feature.user.data.remote.dto.UpdateLocationRequestDto
import com.sociusfit.feature.user.data.remote.dto.UpdateProfileRequestDto
import com.sociusfit.feature.user.domain.model.Result
import com.sociusfit.feature.user.domain.model.User
import com.sociusfit.feature.user.domain.repository.UserRepository
import timber.log.Timber

class UserRepositoryImpl(
    private val userApiService: UserApiService
) : UserRepository {

    override suspend fun updateProfile(firstName: String, lastName: String): Result<User> {
        return try {
            val response = userApiService.updateProfile(
                UpdateProfileRequestDto(firstName, lastName)
            )

            if (response.isSuccessful && response.body()?.isSuccess == true) {
                Result.Success(response.body()!!.data!!.toDomain())
            } else {
                val errors = response.body()?.errors ?: listOf("Errore durante l'aggiornamento")
                Result.Error(errors.firstOrNull() ?: "Errore durante l'aggiornamento")
            }
        } catch (e: Exception) {
            Timber.e(e, "Update profile error")
            Result.Error(e.message ?: "Errore di connessione")
        }
    }

    override suspend fun updateLocation(municipalityCode: String?, maxDistance: Int?): Result<User> {
        return try {
            val response = userApiService.updateLocation(
                UpdateLocationRequestDto(municipalityCode, maxDistance)
            )

            if (response.isSuccessful && response.body()?.isSuccess == true) {
                Result.Success(response.body()!!.data!!.toDomain())
            } else {
                val errors = response.body()?.errors ?: listOf("Errore durante l'aggiornamento")
                Result.Error(errors.firstOrNull() ?: "Errore durante l'aggiornamento")
            }
        } catch (e: Exception) {
            Timber.e(e, "Update location error")
            Result.Error(e.message ?: "Errore di connessione")
        }
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            val response = userApiService.deleteAccount()

            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error("Errore durante l'eliminazione dell'account")
            }
        } catch (e: Exception) {
            Timber.e(e, "Delete account error")
            Result.Error(e.message ?: "Errore di connessione")
        }
    }
}