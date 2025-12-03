package com.sociusfit.core.network.api

import com.sociusfit.core.network.dto.discovery.ProfileCardDto
import com.sociusfit.core.network.dto.discovery.SwipeLikeRequest
import com.sociusfit.core.network.dto.discovery.SwipeLikeResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DiscoveryApiService {
    @GET("discovery/cards")
    suspend fun getDiscoveryCards(
        @Query("sport") sportId: String? = null,
        @Query("limit") limit: Int = 20
    ): List<ProfileCardDto>

    @POST("discovery/like")
    suspend fun swipeLike(@Body request: SwipeLikeRequest): SwipeLikeResponse
}