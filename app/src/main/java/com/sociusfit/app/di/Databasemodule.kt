package com.sociusfit.app.di

import androidx.room.Room
import com.sociusfit.app.data.local.database.SociusFitDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            SociusFitDatabase::class.java,
            SociusFitDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<SociusFitDatabase>().userDao() }
}