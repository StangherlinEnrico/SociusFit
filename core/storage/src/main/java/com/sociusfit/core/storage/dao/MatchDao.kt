package com.sociusfit.core.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sociusfit.core.storage.entity.MatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches ORDER BY createdAt DESC")
    fun getAllMatchesFlow(): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches ORDER BY createdAt DESC")
    suspend fun getAllMatches(): List<MatchEntity>

    @Query("SELECT * FROM matches WHERE id = :matchId")
    suspend fun getMatch(matchId: String): MatchEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(matches: List<MatchEntity>)

    @Update
    suspend fun updateMatch(match: MatchEntity)

    @Query("SELECT COUNT(*) FROM matches WHERE seen = 0 AND createdAt > :since")
    suspend fun getNewMatchCount(since: Long): Int

    @Query("DELETE FROM matches")
    suspend fun clearAll()
}