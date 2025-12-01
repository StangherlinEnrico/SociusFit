package com.sociusfit.feature.user.domain.model

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val profileComplete: Boolean,
    val createdAt: String? = null,
    val updatedAt: String? = null
)