package com.sociusfit.app.di

import androidx.room.Room
import com.sociusfit.core.storage.PreferencesManager
import com.sociusfit.core.storage.SociusFitDatabase
import com.sociusfit.core.storage.provider.MunicipalityProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Storage Module - Koin - UPDATED
 *
 * Configura database Room, PreferencesManager e MunicipalityProvider
 */
val storageModule = module {

    // ========== ROOM DATABASE ==========

    single<SociusFitDatabase> {
        Room.databaseBuilder(
            androidContext(),
            SociusFitDatabase::class.java,
            "sociusfit_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    // ========== DAO PROVIDERS ==========

    single {
        get<SociusFitDatabase>().userDao()
    }

    single {
        get<SociusFitDatabase>().profileDao()
    }

    single {
        get<SociusFitDatabase>().sportDao()
    }

    single {
        get<SociusFitDatabase>().matchDao()
    }

    single {
        get<SociusFitDatabase>().messageDao()
    }

    // ========== PREFERENCES ==========

    single {
        PreferencesManager(androidContext())
    }

    // ========== MUNICIPALITY PROVIDER ==========

    /**
     * MunicipalityProvider
     *
     * Provider per comuni italiani da municipalities.json
     * Usato da OnboardingBioViewModel per autocomplete città
     */
    single {
        MunicipalityProvider(androidContext())
    }
}