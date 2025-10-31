package com.sociusfit.app.data.repository

import com.sociusfit.app.core.util.Result
import com.sociusfit.app.core.util.resultOf
import com.sociusfit.app.data.local.database.dao.UserDao
import com.sociusfit.app.data.local.database.entity.toEntity
import com.sociusfit.app.data.local.datastore.DataStoreManager
import com.sociusfit.app.data.remote.api.AuthApiService
import com.sociusfit.app.data.remote.dto.AvailabilityDto
import com.sociusfit.app.data.remote.dto.LoginRequest
import com.sociusfit.app.data.remote.dto.LoginResponse
import com.sociusfit.app.data.remote.dto.RefreshTokenRequest
import com.sociusfit.app.data.remote.dto.RegisterRequest
import com.sociusfit.app.data.remote.dto.RegisterResponse
import com.sociusfit.app.data.remote.dto.UserDto
import com.sociusfit.app.data.remote.dto.UserSportDto
import com.sociusfit.app.domain.model.User
import com.sociusfit.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import com.sociusfit.app.data.local.database.entity.toDomain

class AuthRepositoryImpl(
    private val api: AuthApiService,
    private val dataStore: DataStoreManager,
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<LoginResponse> = resultOf {
        val response = api.login(LoginRequest(email, password))
        dataStore.saveAuthToken(response.token)
        dataStore.saveRefreshToken(response.refreshToken)
        dataStore.saveUserId(response.userId)

        val user = response.user.toDomain()
        userDao.insertUser(user.toEntity())

        response
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<RegisterResponse> = resultOf {
        val response = api.register(RegisterRequest(name, email, password))
        dataStore.saveAuthToken(response.token)
        dataStore.saveRefreshToken(response.refreshToken)
        dataStore.saveUserId(response.userId)

        val user = response.user.toDomain()
        userDao.insertUser(user.toEntity())

        response
    }

    override suspend fun refreshToken(): Result<String> = resultOf {
        val currentRefreshToken = dataStore.refreshToken.first()
            ?: throw Exception("No refresh token available")

        val response = api.refreshToken(RefreshTokenRequest(currentRefreshToken))
        dataStore.saveAuthToken(response.token)
        dataStore.saveRefreshToken(response.refreshToken)

        response.token
    }

    override suspend fun validateToken(): Result<Boolean> = resultOf {
        val response = api.validateToken()
        response.valid
    }

    override suspend fun logout(): Result<Unit> = resultOf {
        dataStore.clearAll()
        userDao.deleteAllUsers()
    }

    override suspend fun getCurrentUser(): Result<User?> = resultOf {
        val userId = dataStore.userId.first()
        userId?.let { userDao.getUserById(it)?.toDomain() }
    }
}

private fun UserDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        email = email,
        city = city ?: "",
        radiusKm = radiusKm ?: 10,
        sports = sports?.map { it.toDomain() } ?: emptyList(),
        availability = availability?.map { it.toDomain() } ?: emptyList(),
        profileImageUrl = profileImageUrl
    )
}

private fun UserSportDto.toDomain(): com.sociusfit.app.domain.model.UserSport {
    return com.sociusfit.app.domain.model.UserSport(
        sportId = sportId,
        sportName = sportName,
        level = com.sociusfit.app.domain.model.SportLevel.fromString(level)
            ?: com.sociusfit.app.domain.model.SportLevel.BEGINNER
    )
}

private fun AvailabilityDto.toDomain(): com.sociusfit.app.domain.model.Availability {
    return com.sociusfit.app.domain.model.Availability(
        dayOfWeek = java.time.DayOfWeek.valueOf(dayOfWeek),
        timeSlot = com.sociusfit.app.domain.model.TimeSlot.fromString(timeSlot)
            ?: com.sociusfit.app.domain.model.TimeSlot.MORNING
    )
}