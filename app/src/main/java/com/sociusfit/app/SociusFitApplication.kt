package com.sociusfit.app

import android.app.Application
import android.content.pm.ApplicationInfo
import com.sociusfit.app.di.appModule
import com.sociusfit.app.di.networkModule
import com.sociusfit.app.di.storageModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class SociusFitApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if ((applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@SociusFitApplication)
            modules(
                networkModule,
                storageModule,
                appModule
            )
        }
    }
}