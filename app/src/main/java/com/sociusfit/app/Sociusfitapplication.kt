package com.sociusfit.app

import android.app.Application
import com.sociusfit.app.di.dataStoreModule
import com.sociusfit.app.di.databaseModule
import com.sociusfit.app.di.networkModule
import com.sociusfit.app.di.repositoryModule
import com.sociusfit.app.di.useCaseModule
import com.sociusfit.app.di.viewModelModule
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