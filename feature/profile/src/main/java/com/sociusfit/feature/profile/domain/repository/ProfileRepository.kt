package com.sociusfit.feature.profile.domain.repository

import com.sociusfit.core.domain.Result
import com.sociusfit.feature.profile.domain.model.Profile
import kotlinx.coroutines.flow.Flow
import java.io.File

/**
 * Repository interface per gestione profilo
 */
interface ProfileRepository {

    /**
     * Crea un nuovo profilo utente
     */
    suspend fun createProfile(profile: Profile): Result<Profile>

    /**
     * Aggiorna il profilo esistente
     */
    suspend fun updateProfile(profile: Profile): Result<Profile>

    /**
     * Ottiene il profilo dell'utente corrente
     */
    suspend fun getMyProfile(): Result<Profile>

    /**
     * Osserva i cambiamenti del profilo corrente
     */
    fun observeMyProfile(): Flow<Profile?>

    /**
     * Ottiene il profilo di un altro utente
     */
    suspend fun getProfileByUserId(userId: String): Result<Profile>

    /**
     * Carica una foto profilo
     * @return URL della foto caricata
     */
    suspend fun uploadPhoto(photoFile: File): Result<String>
}