package com.sociusfit.core.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sociusfit.core.storage.dao.MatchDao
import com.sociusfit.core.storage.dao.MessageDao
import com.sociusfit.core.storage.dao.ProfileDao
import com.sociusfit.core.storage.dao.SportDao
import com.sociusfit.core.storage.dao.UserDao
import com.sociusfit.core.storage.entity.MatchEntity
import com.sociusfit.core.storage.entity.MessageEntity
import com.sociusfit.core.storage.entity.ProfileEntity
import com.sociusfit.core.storage.entity.ProfileSportEntity
import com.sociusfit.core.storage.entity.SportEntity
import com.sociusfit.core.storage.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ProfileEntity::class,
        SportEntity::class,
        ProfileSportEntity::class,
        MatchEntity::class,
        MessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SociusFitDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun profileDao(): ProfileDao
    abstract fun sportDao(): SportDao
    abstract fun matchDao(): MatchDao
    abstract fun messageDao(): MessageDao
}