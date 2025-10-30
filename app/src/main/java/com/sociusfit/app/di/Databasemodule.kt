package com.sociusfit.app.di

import android.content.Context
import androidx.room.Room
import com.sociusfit.app.data.local.database.SociusFitDatabase
import com.sociusfit.app.data.local.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Modulo Hilt per fornire le dipendenze del database Room
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Fornisce l'istanza del database Room
     */
    @Provides
    @Singleton
    fun provideSociusFitDatabase(
        @ApplicationContext context: Context
    ): SociusFitDatabase {
        return Room.databaseBuilder(
            context,
            SociusFitDatabase::class.java,
            SociusFitDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration() // Per sviluppo, rimuovere in produzione
            .build()
    }

    /**
     * Fornisce il UserDao
     */
    @Provides
    @Singleton
    fun provideUserDao(database: SociusFitDatabase): UserDao {
        return database.userDao()
    }
}