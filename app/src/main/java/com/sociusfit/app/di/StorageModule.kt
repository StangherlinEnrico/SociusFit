package com.sociusfit.app.di

import androidx.room.Room
import com.sociusfit.core.storage.PreferencesManager
import com.sociusfit.core.storage.SociusFitDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Storage Module - Koin
 *
 * Configura database Room e PreferencesManager.
 * Responsabilità:
 * - Inizializzazione database SociusFit
 * - Provisioning di tutti i DAO
 * - PreferencesManager per token storage
 */
val storageModule = module {

    // ========== ROOM DATABASE ==========

    /**
     * SociusFitDatabase
     *
     * Database Room con tutte le entities:
     * - User
     * - Profile, ProfileSport, Sport
     * - Match
     * - Message
     */
    single<SociusFitDatabase> {
        Room.databaseBuilder(
            androidContext(),
            SociusFitDatabase::class.java,
            "sociusfit_database"
        )
            .fallbackToDestructiveMigration() // Durante sviluppo
            // TODO: Aggiungere migrations per produzione
            .build()
    }

    // ========== DAO PROVIDERS ==========

    /**
     * UserDao
     * Accesso alla tabella User
     */
    single {
        get<SociusFitDatabase>().userDao()
    }

    /**
     * ProfileDao
     * Accesso alle tabelle Profile, ProfileSport
     */
    single {
        get<SociusFitDatabase>().profileDao()
    }

    /**
     * SportDao
     * Accesso alla tabella Sport
     */
    single {
        get<SociusFitDatabase>().sportDao()
    }

    /**
     * MatchDao
     * Accesso alla tabella Match
     */
    single {
        get<SociusFitDatabase>().matchDao()
    }

    /**
     * MessageDao
     * Accesso alla tabella Message
     */
    single {
        get<SociusFitDatabase>().messageDao()
    }

    // ========== PREFERENCES ==========

    /**
     * PreferencesManager
     *
     * Gestione DataStore per:
     * - Token JWT
     * - User preferences
     * - App settings
     */
    single {
        PreferencesManager(androidContext())
    }
}