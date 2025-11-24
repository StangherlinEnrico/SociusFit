package com.sociusfit.app.di

import com.sociusfit.app.data.local.DataStoreManager
import com.sociusfit.app.data.repository.LocationRepositoryImpl
import com.sociusfit.app.domain.repository.LocationRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Koin module for local data storage dependencies
 * Provides DataStoreManager for preferences
 */
val dataModule = module {

    // DataStore Manager
    single {
        DataStoreManager(androidContext())
    }

    // Location Repository
    single<LocationRepository> {
        LocationRepositoryImpl(
            context = androidContext(),
            gson = get()
        )
    }
}