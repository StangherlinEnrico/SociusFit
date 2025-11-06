package com.sociusfit.app.data.repository

import com.sociusfit.app.data.local.DataStoreManager
import com.sociusfit.app.data.remote.api.UpdateLocationRequest
import com.sociusfit.app.data.remote.api.UpdateProfileRequest
import com.sociusfit.app.data.remote.api.UserApiService
import com.sociusfit.app.data.remote.mapper.UserMapper
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.model.User
import com.sociusfit.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import timber.log.Timber

/**
 * Implementation of UserRepository
 * Handles user operations with backend API
 */
class UserRepositoryImpl(
    private val userApiService: UserApiService,
    private val dataStoreManager: DataStoreManager
) : UserRepository {

    override suspend fun getUserById(userId: Int): Result<User> {
        return try {
            Timber.d("Fetching user by ID: $userId")

            val response = userApiService.getUserById(userId)

            if (response.isSuccessful) {
                val apiResult = response.body()

                if (apiResult?.success == true && apiResult.data != null) {
                    val user = UserMapper.toDomain(apiResult.data)

                    Timber.i("User fetched successfully: ${user.email}")
                    Result.Success(user, apiResult.message)
                } else {
                    val errorMessage = apiResult?.message ?: "Failed to fetch user"
                    Timber.w("Fetch user failed: $errorMessage")
                    Result.Error(errorMessage)
                }
            } else {
                val errorMessage = "HTTP ${response.code()}: ${response.message()}"
                Timber.e("Fetch user request failed: $errorMessage")
                Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception fetching user by ID")
            Result.Error(e.message ?: "Network error occurred")
        }
    }

    override suspend fun getCurrentUser(): Result<User> {
        return try {
            // Get current user ID from DataStore
            val userId = dataStoreManager.userId.first()

            if (userId == null) {
                Timber.w("No user ID found in DataStore")
                return Result.Error("User not logged in")
            }

            Timber.d("Fetching current user: $userId")
            getUserById(userId)
        } catch (e: Exception) {
            Timber.e(e, "Exception fetching current user")
            Result.Error(e.message ?: "Failed to get current user")
        }
    }

    override suspend fun updateProfile(
        firstName: String,
        lastName: String,
        location: String?
    ): Result<User> {
        return try {
            Timber.d("Updating user profile")

            val response = userApiService.updateProfile(
                UpdateProfileRequest(
                    firstName = firstName,
                    lastName = lastName,
                    location = location
                )
            )

            if (response.isSuccessful) {
                val apiResult = response.body()

                if (apiResult?.success == true && apiResult.data != null) {
                    val user = UserMapper.toDomain(apiResult.data)

                    // Update local user data
                    dataStoreManager.saveUserData(
                        userId = user.id,
                        email = user.email,
                        firstName = user.firstName,
                        lastName = user.lastName
                    )

                    Timber.i("Profile updated successfully")
                    Result.Success(user, apiResult.message)
                } else {
                    val errorMessage = apiResult?.message ?: "Failed to update profile"
                    Timber.w("Update profile failed: $errorMessage")
                    Result.Error(errorMessage)
                }
            } else {
                val errorMessage = "HTTP ${response.code()}: ${response.message()}"
                Timber.e("Update profile request failed: $errorMessage")
                Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception updating profile")
            Result.Error(e.message ?: "Network error occurred")
        }
    }

    override suspend fun updateLocation(
        latitude: Double,
        longitude: Double,
        maxDistanceKm: Int
    ): Result<User> {
        return try {
            Timber.d("Updating user location: lat=$latitude, lon=$longitude, maxDistance=$maxDistanceKm")

            val response = userApiService.updateLocation(
                UpdateLocationRequest(
                    latitude = latitude,
                    longitude = longitude,
                    maxDistanceKm = maxDistanceKm
                )
            )

            if (response.isSuccessful) {
                val apiResult = response.body()

                if (apiResult?.success == true && apiResult.data != null) {
                    val user = UserMapper.toDomain(apiResult.data)

                    Timber.i("Location updated successfully")
                    Result.Success(user, apiResult.message)
                } else {
                    val errorMessage = apiResult?.message ?: "Failed to update location"
                    Timber.w("Update location failed: $errorMessage")
                    Result.Error(errorMessage)
                }
            } else {
                val errorMessage = "HTTP ${response.code()}: ${response.message()}"
                Timber.e("Update location request failed: $errorMessage")
                Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception updating location")
            Result.Error(e.message ?: "Network error occurred")
        }
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            Timber.d("Deleting user account")

            val response = userApiService.deleteAccount()

            if (response.isSuccessful) {
                val apiResult = response.body()

                if (apiResult?.success == true) {
                    // Clear all local data after account deletion
                    dataStoreManager.clearAll()

                    Timber.i("Account deleted successfully")
                    Result.Success(Unit, apiResult.message)
                } else {
                    val errorMessage = apiResult?.message ?: "Failed to delete account"
                    Timber.w("Delete account failed: $errorMessage")
                    Result.Error(errorMessage)
                }
            } else {
                val errorMessage = "HTTP ${response.code()}: ${response.message()}"
                Timber.e("Delete account request failed: $errorMessage")
                Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception deleting account")
            Result.Error(e.message ?: "Network error occurred")
        }
    }
}