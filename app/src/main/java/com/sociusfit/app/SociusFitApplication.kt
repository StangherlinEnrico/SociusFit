package com.sociusfit.app

import android.app.Application
import com.sociusfit.app.di.*
import com.sociusfit.feature.user.di.userModule
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
                userModule,
                dataModule,
                networkModule,
                repositoryModule,
                domainModule,
                presentationModule,
                useCaseModule
            )
        }

        Timber.d("SociusFit Application started with Koin")
    }
}