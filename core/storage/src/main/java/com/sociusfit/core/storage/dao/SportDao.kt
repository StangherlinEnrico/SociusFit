package com.sociusfit.core.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sociusfit.core.storage.entity.SportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SportDao {
    @Query("SELECT * FROM sports")
    fun getAllSportsFlow(): Flow<List<SportEntity>>

    @Query("SELECT * FROM sports")
    suspend fun getAllSports(): List<SportEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSports(sports: List<SportEntity>)

    @Query("DELETE FROM sports")
    suspend fun clearAll()
}