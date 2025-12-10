package com.sociusfit.feature.profile.domain.repository

import com.sociusfit.core.domain.Result
import com.sociusfit.feature.profile.domain.model.Sport
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface per gestione sport
 */
interface SportsRepository {

    /**
     * Ottiene tutti gli sport disponibili
     */
    suspend fun getAllSports(): Result<List<Sport>>

    /**
     * Osserva tutti gli sport disponibili
     */
    fun observeAllSports(): Flow<List<Sport>>
}