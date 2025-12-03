package com.sociusfit.core.network.api

import com.sociusfit.core.network.dto.notifications.RegisterTokenRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationsApiService {
    @POST("notifications/register-token")
    suspend fun registerToken(@Body request: RegisterTokenRequest)
}