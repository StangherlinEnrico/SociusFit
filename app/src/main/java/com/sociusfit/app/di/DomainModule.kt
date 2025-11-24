package com.sociusfit.app.di

import com.sociusfit.app.domain.usecase.auth.*
import com.sociusfit.app.domain.usecase.location.*
import com.sociusfit.app.domain.usecase.user.*
import org.koin.dsl.module

/**
 * Koin module for domain layer use cases
 * Provides all use cases with their repository dependencies
 */
val domainModule = module {

    // Auth Use Cases
    factory { LoginUseCase(authRepository = get()) }
    factory { RegisterUseCase(authRepository = get()) }
    factory { LoginWithOAuthUseCase(authRepository = get()) }
    factory { ForgotPasswordUseCase(authRepository = get()) }
    factory { LogoutUseCase(authRepository = get()) }
    factory { CheckLoginStatusUseCase(authRepository = get()) }

    // User Use Cases
    factory { GetCurrentUserUseCase(userRepository = get()) }
    factory { UpdateProfileUseCase(userRepository = get()) }
    factory { UpdateLocationUseCase(userRepository = get()) }
    factory { DeleteAccountUseCase(userRepository = get()) }

    // Location Use Cases
    factory { GetAllRegionsUseCase(locationRepository = get()) }
    factory { SearchMunicipalitiesUseCase(locationRepository = get()) }
    factory { GetMunicipalitiesByRegionUseCase(locationRepository = get()) }
}