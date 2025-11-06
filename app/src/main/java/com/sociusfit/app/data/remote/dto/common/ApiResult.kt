package com.sociusfit.app.data.remote.dto.common

import com.google.gson.annotations.SerializedName

/**
 * Generic API response wrapper
 * Maps to backend Result<T> structure
 */
data class ApiResult<T>(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: T? = null,

    @SerializedName("errors")
    val errors: List<String>? = null
)