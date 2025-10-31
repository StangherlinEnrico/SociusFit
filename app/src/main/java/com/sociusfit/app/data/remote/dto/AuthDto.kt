package com.sociusfit.app.data.remote.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val refreshToken: String,
    val userId: String,
    val user: UserDto
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class RegisterResponse(
    val token: String,
    val refreshToken: String,
    val userId: String,
    val user: UserDto
)

data class RefreshTokenRequest(
    val refreshToken: String
)

data class RefreshTokenResponse(
    val token: String,
    val refreshToken: String
)

data class ValidateTokenResponse(
    val valid: Boolean,
    val userId: String?
)

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val city: String? = null,
    val radiusKm: Int? = null,
    val profileImageUrl: String? = null,
    val sports: List<UserSportDto>? = null,
    val availability: List<AvailabilityDto>? = null
)

data class UserSportDto(
    val sportId: String,
    val sportName: String,
    val level: String
)

data class AvailabilityDto(
    val dayOfWeek: String,
    val timeSlot: String
)