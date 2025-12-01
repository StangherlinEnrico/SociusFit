package com.sociusfit.feature.user.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ApiResult<T>(
    @SerializedName("data") val data: T? = null,
    @SerializedName("errors") val errors: List<String>? = null,
    @SerializedName("isSuccess") val isSuccess: Boolean = true
)