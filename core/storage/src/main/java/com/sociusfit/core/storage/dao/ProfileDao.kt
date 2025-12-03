package com.sociusfit.core.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sociusfit.core.storage.entity.ProfileEntity
import com.sociusfit.core.storage.entity.ProfileSportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles WHERE userId = :userId")
    fun getProfileFlow(userId: String): Flow<ProfileEntity?>

    @Query("SELECT * FROM profiles WHERE userId = :userId")
    suspend fun getProfile(userId: String): ProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity)

    @Query("SELECT * FROM profile_sports WHERE profileId = :profileId")
    suspend fun getProfileSports(profileId: String): List<ProfileSportEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileSports(sports: List<ProfileSportEntity>)

    @Query("DELETE FROM profile_sports WHERE profileId = :profileId")
    suspend fun deleteProfileSports(profileId: String)

    @Transaction
    suspend fun updateProfileWithSports(profile: ProfileEntity, sports: List<ProfileSportEntity>) {
        insertProfile(profile)
        deleteProfileSports(profile.id)
        insertProfileSports(sports)
    }

    @Query("DELETE FROM profiles")
    suspend fun clearAll()
}