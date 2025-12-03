package com.sociusfit.core.network.api

import com.sociusfit.core.network.dto.chat.ConversationDto
import com.sociusfit.core.network.dto.chat.MessageDto
import com.sociusfit.core.network.dto.chat.SendMessageRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApiService {
    @GET("chat/conversations")
    suspend fun getConversations(): List<ConversationDto>

    @GET("chat/{matchId}/messages")
    suspend fun getMessages(
        @Path("matchId") matchId: String,
        @Query("pageSize") pageSize: Int = 50,
        @Query("pageNumber") pageNumber: Int = 1
    ): List<MessageDto>

    @POST("chat/{matchId}/messages")
    suspend fun sendMessage(
        @Path("matchId") matchId: String,
        @Body request: SendMessageRequest
    ): MessageDto

    @PUT("chat/{matchId}/read")
    suspend fun markAsRead(@Path("matchId") matchId: String)
}