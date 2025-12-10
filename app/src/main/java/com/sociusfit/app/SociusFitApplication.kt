package com.sociusfit.app

import android.app.Application
import com.sociusfit.app.di.appModule
import com.sociusfit.app.di.networkModule
import com.sociusfit.app.di.storageModule
import com.sociusfit.feature.auth.di.authModule
import com.sociusfit.feature.profile.di.profileModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

/**
 * SociusFit Application Class
 *
 * Entry point dell'applicazione Android.
 * Responsabilità:
 * - Inizializzazione Timber per logging
 * - Setup Koin per Dependency Injection
 * - Caricamento moduli Koin
 */
class SociusFitApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Inizializza Timber per logging
        Timber.plant(Timber.DebugTree())

        Timber.d("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        Timber.d("🚀 SociusFit App Starting...")
        Timber.d("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

        // Inizializza Koin per Dependency Injection
        startKoin {
            // Logging Koin
            androidLogger(Level.DEBUG)

            // Contesto Android
            androidContext(this@SociusFitApplication)

            // Carica tutti i moduli Koin
            modules(
                // Core modules
                appModule,
                networkModule,
                storageModule,

                // Feature modules
                authModule,
                profileModule

                // TODO Sprint 3: discoveryModule
                // TODO Sprint 4: matchModule
                // TODO Sprint 5: chatModule
            )
        }

        Timber.d("✅ Koin modules loaded successfully")
        Timber.d("   - Core: app, network, storage")
        Timber.d("   - Features: auth, profile")
    }
}