package com.sociusfit.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sociusfit.app.core.Constants
import com.sociusfit.app.data.local.database.dao.UserDao
import com.sociusfit.app.data.local.database.entity.UserEntity

/**
 * Database Room principale di SociusFit
 */
@Database(
    entities = [
        UserEntity::class
        // TODO: Aggiungere altre entità (SportEntity, MatchEntity, ecc.)
    ],
    version = Constants.DATABASE_VERSION,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class SociusFitDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    // TODO: Aggiungere altri DAO quando necessario

    companion object {
        const val DATABASE_NAME = Constants.DATABASE_NAME
    }
}