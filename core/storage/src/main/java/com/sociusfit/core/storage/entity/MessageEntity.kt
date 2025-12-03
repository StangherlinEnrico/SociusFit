package com.sociusfit.core.storage.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages",
    indices = [Index("matchId"), Index("sentAt")]
)
data class MessageEntity(
    @PrimaryKey val id: String,
    val matchId: String,
    val senderId: String,
    val content: String,
    val sentAt: Long,
    val isRead: Boolean
)