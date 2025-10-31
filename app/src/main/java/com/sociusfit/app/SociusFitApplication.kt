package com.sociusfit.app

import android.app.Application
import com.sociusfit.app.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class SociusFitApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@SociusFitApplication)
            modules(
                networkModule,
                dataStoreModule,
                databaseModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }

        Timber.d("SociusFit Application started with Koin")
    }
}