package com.sociusfit.app.di

import androidx.room.Room
import com.sociusfit.core.storage.PreferencesManager
import com.sociusfit.core.storage.SociusFitDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val storageModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            SociusFitDatabase::class.java,
            "sociusfit_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<SociusFitDatabase>().userDao() }
    single { get<SociusFitDatabase>().profileDao() }
    single { get<SociusFitDatabase>().sportDao() }
    single { get<SociusFitDatabase>().matchDao() }
    single { get<SociusFitDatabase>().messageDao() }

    single { PreferencesManager(androidContext()) }
}