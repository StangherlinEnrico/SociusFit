package com.sociusfit.app.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sociusfit.app.data.local.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object per la gestione degli utenti nel database locale
 */
@Dao
interface UserDao {

    /**
     * Inserisce o aggiorna un utente nel database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    /**
     * Aggiorna un utente esistente
     */
    @Update
    suspend fun updateUser(user: UserEntity)

    /**
     * Elimina un utente
     */
    @Delete
    suspend fun deleteUser(user: UserEntity)

    /**
     * Ottiene un utente per ID
     */
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    /**
     * Ottiene un utente per ID come Flow (aggiornamenti real-time)
     */
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserByIdFlow(userId: String): Flow<UserEntity?>

    /**
     * Ottiene tutti gli utenti
     */
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    /**
     * Ottiene tutti gli utenti come Flow
     */
    @Query("SELECT * FROM users")
    fun getAllUsersFlow(): Flow<List<UserEntity>>

    /**
     * Elimina tutti gli utenti (utile per logout)
     */
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}