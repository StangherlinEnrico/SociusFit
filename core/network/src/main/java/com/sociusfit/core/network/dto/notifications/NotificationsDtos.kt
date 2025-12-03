package com.sociusfit.core.network.dto.notifications

data class RegisterTokenRequest(
    val token: String,
    val platform: String
)