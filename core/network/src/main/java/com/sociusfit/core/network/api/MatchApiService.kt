package com.sociusfit.core.network.api

import com.sociusfit.core.network.dto.match.MatchDto
import retrofit2.http.GET

interface MatchApiService {
    @GET("matches")
    suspend fun getMatches(): List<MatchDto>
}