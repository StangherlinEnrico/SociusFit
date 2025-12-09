package com.sociusfit.core.network.api

import com.sociusfit.core.network.dto.auth.AuthResponse
import com.sociusfit.core.network.dto.auth.LoginRequest
import com.sociusfit.core.network.dto.auth.RegisterRequest
import com.sociusfit.core.network.dto.auth.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Auth API Service
 *
 * Servizio Retrofit per gli endpoint di autenticazione.
 *
 */
interface AuthApiService {

    /**
     * Registra un nuovo utente
     *
     * @param request Dati di registrazione (firstName, lastName, email, password)
     * @return Token JWT e dati utente
     */
    @POST("users/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): AuthResponse

    /**
     * Login utente esistente
     *
     * @param request Credenziali (email, password)
     * @return Token JWT e dati utente
     */
    @POST("users/login")
    suspend fun login(
        @Body request: LoginRequest
    ): AuthResponse

    /**
     * Ottieni utente corrente
     *
     * Richiede header: Authorization: Bearer {token}
     *
     * @return Dati utente corrente
     */
    @GET("users/me")
    suspend fun getCurrentUser(): UserDto

    /**
     * Logout utente corrente
     *
     * Richiede header: Authorization: Bearer {token}
     * Invalida il token JWT sul server.
     *
     * Il backend restituisce 200 OK senza body, quindi usiamo Unit.
     */
    @POST("users/logout")
    suspend fun logout()
}