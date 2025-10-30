package com.sociusfit.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Classe Application principale di SociusFit.
 * Annotata con @HiltAndroidApp per abilitare la dependency injection con Hilt.
 */
@HiltAndroidApp
class SociusFitApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Inizializza Timber per logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.d("SociusFit Application started")
    }
}