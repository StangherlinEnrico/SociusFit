package com.sociusfit.app.data.repository

import com.sociusfit.app.data.local.DataStoreManager
import com.sociusfit.app.data.remote.api.UpdateLocationRequest
import com.sociusfit.app.data.remote.api.UpdateProfileRequest
import com.sociusfit.app.data.remote.api.UserApiService
import com.sociusfit.app.data.remote.mapper.UserMapper
import com.sociusfit.app.domain.model.Result
import com.sociusfit.app.domain.model.User
import com.sociusfit.app.domain.repository.UserRepository
import timber.log.Timber

/**
 * Implementation of UserRepository
 * Handles user operations with backend API
 */
class UserRepositoryImpl(
    private val userApiService: UserApiService,
    private val dataStoreManager: DataStoreManager
) : UserRepository {

    override suspend fun getCurrentUser(): Result<User> {
        return try {
            Timber.d("Fetching current user")

            val response = userApiService.getCurrentUser()

            if (response.isSuccessful) {
                val apiResult = response.body()

                if (apiResult?.success == true && apiResult.data != null) {
                    val user = UserMapper.toDomain(apiResult.data)

                    // Update local cache
                    dataStoreManager.saveUserData(
                        userId = user.id,
                        email = user.email,
                        firstName = user.firstName,
                        lastName = user.lastName
                    )

                    Timber.i("User fetched successfully")
                    Result.Success(user, apiResult.message)
                } else {
                    val errorMessage = apiResult?.message ?: "Failed to fetch user"
                    Timber.w("Get user failed: $errorMessage")
                    Result.Error(errorMessage)
                }
            } else {
                val errorMessage = "HTTP ${response.code()}: ${response.message()}"
                Timber.e("Get user request failed: $errorMessage")
                Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception fetching user")
            Result.Error(e.message ?: "Network error occurred")
        }
    }

    override suspend fun getUserById(userId: Int): Result<User> {
        return try {
            Timber.d("Fetching user by ID: $userId")

            val response = userApiService.getUserById(userId)

            if (response.isSuccessful) {
                val apiResult = response.body()

                if (apiResult?.success == true && apiResult.data != null) {
                    val user = UserMapper.toDomain(apiResult.data)

                    Timber.i("User fetched successfully")
                    Result.Success(user, apiResult.message)
                } else {
                    val errorMessage = apiResult?.message ?: "Failed to fetch user"
                    Timber.w("Get user by ID failed: $errorMessage")
                    Result.Error(errorMessage)
                }
            } else {
                val errorMessage = "HTTP ${response.code()}: ${response.message()}"
                Timber.e("Get user by ID request failed: $errorMessage")
                Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception fetching user by ID")
            Result.Error(e.message ?: "Network error occurred")
        }
    }

    /**
     * Update profile sends ONLY firstName and lastName
     */
    override suspend fun updateProfile(
        firstName: String,
        lastName: String
    ): Result<User> {
        return try {
            Timber.d("Updating user profile: firstName=$firstName, lastName=$lastName")

            val response = userApiService.updateProfile(
                UpdateProfileRequest(
                    firstName = firstName,
                    lastName = lastName
                )
            )

            if (response.isSuccessful) {
                val apiResult = response.body()

                if (apiResult?.success == true && apiResult.data != null) {
                    val user = UserMapper.toDomain(apiResult.data)

                    // Update local cache
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

    /**
     * 🔥 FIXED: Update location sends municipality ISTAT code
     */
    override suspend fun updateLocation(
        locationCode: String?,
        maxDistance: Int?
    ): Result<User> {
        return try {
            Timber.d("Updating user location: locationCode=$locationCode, maxDistance=$maxDistance")

            val response = userApiService.updateLocation(
                UpdateLocationRequest(
                    location = locationCode,  // Send ISTAT code (e.g., "026086")
                    maxDistance = maxDistance
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