package com.sociusfit.feature.user.domain.model

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val createdAt: String,
    val updatedAt: String
)