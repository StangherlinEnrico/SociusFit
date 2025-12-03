package com.sociusfit.core.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sociusfit.core.storage.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE matchId = :matchId ORDER BY sentAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getMessages(matchId: String, limit: Int, offset: Int): List<MessageEntity>

    @Query("SELECT * FROM messages WHERE matchId = :matchId ORDER BY sentAt DESC")
    fun getMessagesFlow(matchId: String): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Update
    suspend fun updateMessage(message: MessageEntity)

    @Query("SELECT COUNT(*) FROM messages WHERE matchId = :matchId AND senderId != :currentUserId AND isRead = 0")
    suspend fun getUnreadCount(matchId: String, currentUserId: String): Int

    @Query("UPDATE messages SET isRead = 1 WHERE matchId = :matchId AND senderId != :currentUserId AND isRead = 0")
    suspend fun markAsRead(matchId: String, currentUserId: String)

    @Query("DELETE FROM messages WHERE matchId = :matchId")
    suspend fun deleteMessages(matchId: String)

    @Query("DELETE FROM messages")
    suspend fun clearAll()
}