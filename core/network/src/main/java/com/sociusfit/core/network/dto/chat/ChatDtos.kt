package com.sociusfit.core.network.dto.chat

data class ConversationDto(
    val matchId: String,
    val otherUserId: String,
    val otherUserName: String,
    val otherUserPhotoUrl: String?,
    val lastMessage: MessageDto?,
    val unreadCount: Int
)

data class MessageDto(
    val id: String,
    val matchId: String,
    val senderId: String,
    val content: String,
    val sentAt: String,
    val isRead: Boolean
)

data class SendMessageRequest(
    val content: String
)