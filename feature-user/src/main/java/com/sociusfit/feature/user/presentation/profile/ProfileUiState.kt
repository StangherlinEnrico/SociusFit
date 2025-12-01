package com.sociusfit.feature.user.presentation.profile

import com.sociusfit.feature.user.domain.model.User

data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val logoutSuccess: Boolean = false
)