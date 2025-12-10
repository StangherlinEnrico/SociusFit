package com.sociusfit.core.storage.dao

import androidx.room.*
import com.sociusfit.core.storage.entity.ProfileEntity
import com.sociusfit.core.storage.entity.ProfileSportEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO per accesso ai profili
 */
@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity)

    @Update
    suspend fun updateProfile(profile: ProfileEntity)

    @Query("SELECT * FROM profiles WHERE userId = :userId")
    suspend fun getProfile(userId: String): ProfileEntity?

    @Query("SELECT * FROM profiles LIMIT 1")
    suspend fun getMyProfile(): ProfileEntity?

    @Query("SELECT * FROM profiles LIMIT 1")
    fun observeMyProfile(): Flow<ProfileEntity?>

    @Delete
    suspend fun deleteProfile(profile: ProfileEntity)

    @Query("DELETE FROM profiles WHERE userId = :userId")
    suspend fun deleteProfileByUserId(userId: String)

    // Profile Sports
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileSport(profileSport: ProfileSportEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileSports(profileSports: List<ProfileSportEntity>)

    @Query("SELECT * FROM profile_sports WHERE userId = :userId")
    suspend fun getProfileSports(userId: String): List<ProfileSportEntity>

    @Query("DELETE FROM profile_sports WHERE userId = :userId")
    suspend fun deleteProfileSportsByUserId(userId: String)

    @Query("DELETE FROM profile_sports WHERE userId = :userId AND sportId = :sportId")
    suspend fun deleteProfileSport(userId: String, sportId: String)

    /**
     * Transaction per aggiornare profilo e sport insieme
     */
    @Transaction
    suspend fun updateProfileWithSports(
        profile: ProfileEntity,
        sports: List<ProfileSportEntity>
    ) {
        updateProfile(profile)
        deleteProfileSportsByUserId(profile.userId)
        insertProfileSports(sports)
    }
}