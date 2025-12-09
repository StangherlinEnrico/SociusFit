package com.sociusfit.app

import android.app.Application
import com.sociusfit.app.di.networkModule
import com.sociusfit.app.di.storageModule
import com.sociusfit.feature.auth.di.authModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class SociusFitApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Inizializza Timber
        Timber.plant(Timber.DebugTree())

        Timber.d("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        Timber.d("SociusFit App Starting...")
        Timber.d("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

        // Inizializza Koin
        startKoin {
            androidContext(this@SociusFitApplication)
            modules(
                networkModule,
                storageModule,
                authModule
            )
        }

        Timber.d("Koin modules loaded")
    }
}