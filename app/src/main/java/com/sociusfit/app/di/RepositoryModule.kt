package com.sociusfit.app.di

import com.sociusfit.app.data.repository.AuthRepositoryImpl
import com.sociusfit.app.domain.repository.AuthRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
}