package com.sociusfit.feature.profile.data.repository

import com.sociusfit.core.domain.Result
import com.sociusfit.core.network.api.SportsApiService
import com.sociusfit.core.storage.dao.SportDao
import com.sociusfit.feature.profile.data.mapper.SportsMapper
import com.sociusfit.feature.profile.domain.model.Sport
import com.sociusfit.feature.profile.domain.repository.SportsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

/**
 * Implementazione repository sport
 * Gestisce cache locale degli sport disponibili
 */
class SportsRepositoryImpl(
    private val apiService: SportsApiService,
    private val sportDao: SportDao,
    private val mapper: SportsMapper
) : SportsRepository {

    override suspend fun getAllSports(): Result<List<Sport>> {
        return try {
            // API ritorna direttamente List<SportDto>
            val response = apiService.getSports()

            // Cache in Room
            val entities = response.map { mapper.toEntity(it) }
            sportDao.insertSports(entities)

            val sports = response.map { mapper.toDomain(it) }
            Result.Success(sports)
        } catch (e: Exception) {
            Timber.e(e, "Error getting sports")

            // Try to load from cache
            val cachedEntities = sportDao.getAllSports()
            if (cachedEntities.isNotEmpty()) {
                val cachedSports = cachedEntities.map { mapper.fromEntity(it) }
                return Result.Success(cachedSports)
            }

            Result.Error(Exception("Failed to get sports", e))
        }
    }

    override fun observeAllSports(): Flow<List<Sport>> {
        return sportDao.observeAllSports()
            .map { entities ->
                entities.map { mapper.fromEntity(it) }
            }
    }
}