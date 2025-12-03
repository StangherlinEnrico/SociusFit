package com.sociusfit.core.network.api

import com.sociusfit.core.network.dto.sports.SportDto
import retrofit2.http.GET

interface SportsApiService {
    @GET("sports")
    suspend fun getSports(): List<SportDto>
}