package com.sociusfit.app

import android.app.Application
import com.sociusfit.app.di.appModule
import com.sociusfit.app.di.networkModule
import com.sociusfit.feature.user.di.userModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class SociusFitApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (Constants.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@SociusFitApplication)
            modules(
                appModule,
                networkModule,
                userModule
            )
        }

        Timber.d("SociusFit Application started")
    }
}