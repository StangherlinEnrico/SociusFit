package com.sociusfit.feature.user.domain.repository

import com.sociusfit.feature.user.domain.model.Result
import com.sociusfit.feature.user.domain.model.User

interface UserRepository {
    suspend fun updateProfile(firstName: String, lastName: String): Result<User>
    suspend fun updateLocation(municipalityCode: String?, maxDistance: Int?): Result<User>
    suspend fun deleteAccount(): Result<Unit>
}