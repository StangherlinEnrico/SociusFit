package com.sociusfit.app.di

import android.content.Context
import com.sociusfit.app.data.local.datastore.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Modulo Hilt per fornire le dipendenze di DataStore
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    /**
     * Fornisce l'istanza del DataStoreManager
     */
    @Provides
    @Singleton
    fun provideDataStoreManager(
        @ApplicationContext context: Context
    ): DataStoreManager {
        return DataStoreManager(context)
    }
}