package com.sociusfit.feature.user.data.mapper

import com.sociusfit.feature.user.data.remote.dto.UserDto
import com.sociusfit.feature.user.domain.model.User

fun UserDto.toDomain(): User = User(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    createdAt = createdAt,
    updatedAt = updatedAt
)