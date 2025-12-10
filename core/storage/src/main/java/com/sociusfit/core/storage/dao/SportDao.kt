package com.sociusfit.core.storage.dao

import androidx.room.*
import com.sociusfit.core.storage.entity.SportEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO per accesso agli sport
 */
@Dao
interface SportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSport(sport: SportEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSports(sports: List<SportEntity>)

    @Query("SELECT * FROM sports ORDER BY name ASC")
    suspend fun getAllSports(): List<SportEntity>

    @Query("SELECT * FROM sports ORDER BY name ASC")
    fun observeAllSports(): Flow<List<SportEntity>>

    @Query("SELECT * FROM sports WHERE id = :sportId")
    suspend fun getSportById(sportId: String): SportEntity?

    @Query("DELETE FROM sports")
    suspend fun deleteAllSports()
}