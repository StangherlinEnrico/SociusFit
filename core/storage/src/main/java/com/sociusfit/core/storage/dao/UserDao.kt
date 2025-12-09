package com.sociusfit.core.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sociusfit.core.storage.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    /**
     * Ottiene l'utente come Flow (osservabile)
     */
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserFlow(userId: String): Flow<UserEntity?>

    /**
     * Ottiene l'utente in modo sincrono
     */
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUser(userId: String): UserEntity?

    /**
     * Ottiene l'utente corrente (assumendo single user app)
     * Usato per recuperare l'utente loggato
     */
    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    /**
     * Osserva l'utente corrente come Flow
     * Usato dal repository per fornire Flow<User?>
     */
    @Query("SELECT * FROM users LIMIT 1")
    fun observeCurrentUser(): Flow<UserEntity?>

    /**
     * Inserisce o aggiorna un utente
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    /**
     * Cancella tutti gli utenti (usato durante logout)
     */
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    /**
     * Cancella tutti gli utenti (alias per compatibilità)
     */
    @Query("DELETE FROM users")
    suspend fun clearAll()
}