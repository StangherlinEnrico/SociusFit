package com.sociusfit.app.di

import com.sociusfit.app.data.repository.AuthRepositoryImpl
import com.sociusfit.app.data.repository.UserRepositoryImpl
import com.sociusfit.app.domain.repository.AuthRepository
import com.sociusfit.app.domain.repository.UserRepository
import org.koin.dsl.module

/**
 * Koin module for repository implementations
 * Provides repositories with API services and DataStoreManager
 */
val repositoryModule = module {

    // Auth Repository
    single<AuthRepository> {
        AuthRepositoryImpl(
            authApiService = get(),
            dataStoreManager = get()
        )
    }

    // User Repository
    single<UserRepository> {
        UserRepositoryImpl(
            userApiService = get(),
            dataStoreManager = get()
        )
    }
}