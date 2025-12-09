package com.sociusfit.feature.auth.domain.repository

import com.sociusfit.core.domain.Result
import com.sociusfit.core.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Auth Repository Interface
 *
 * Definisce il contratto per le operazioni di autenticazione.
 * Seguendo il principio di Dependency Inversion, il domain layer
 * definisce l'interfaccia che il data layer implementa.
 */
interface AuthRepository {

    /**
     * Registra un nuovo utente
     *
     * @param firstName Nome dell'utente
     * @param lastName Cognome dell'utente
     * @param email Email dell'utente
     * @param password Password dell'utente
     * @return Result<User> Success con User o Error con Exception
     */
    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<User>

    /**
     * Effettua login
     *
     * @param email Email dell'utente
     * @param password Password dell'utente
     * @return Result<User> Success con User o Error con Exception
     */
    suspend fun login(
        email: String,
        password: String
    ): Result<User>

    /**
     * Effettua logout
     *
     * Invalida il token sul backend e rimuove i dati locali
     *
     * @return Result<Unit> Success o Error con Exception
     */
    suspend fun logout(): Result<Unit>

    /**
     * Ottiene l'utente corrente
     *
     * Verifica se esiste un token valido e recupera i dati utente
     *
     * @return Result<User> Success con User o Error se non autenticato
     */
    suspend fun getCurrentUser(): Result<User>

    /**
     * Verifica se l'utente è autenticato
     *
     * @return true se esiste un token valido salvato localmente
     */
    suspend fun isAuthenticated(): Boolean

    /**
     * Observable dell'utente corrente
     *
     * Emette User quando autenticato, null quando non autenticato
     *
     * @return Flow<User?> Observable dello stato di autenticazione
     */
    fun observeCurrentUser(): Flow<User?>

    /**
     * Salva il token JWT
     *
     * @param token JWT token da salvare
     */
    suspend fun saveToken(token: String)

    /**
     * Ottiene il token JWT salvato
     *
     * @return Token JWT o null se non presente
     */
    suspend fun getToken(): String?

    /**
     * Cancella il token JWT
     */
    suspend fun clearToken()
}