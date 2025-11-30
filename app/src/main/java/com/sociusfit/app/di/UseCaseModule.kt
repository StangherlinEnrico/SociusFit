// File: app/src/main/java/com/sociusfit/app/di/UseCaseModule.kt

package com.sociusfit.app.di

import com.sociusfit.app.domain.usecase.auth.*
import com.sociusfit.app.domain.usecase.location.GetAllRegionsUseCase
import com.sociusfit.app.domain.usecase.location.GetMunicipalitiesByRegionUseCase
import com.sociusfit.app.domain.usecase.location.GetMunicipalityByCodeUseCase  // 🔥 ADD THIS
import com.sociusfit.app.domain.usecase.location.SearchMunicipalitiesUseCase
import com.sociusfit.app.domain.usecase.user.*
import org.koin.dsl.module

/**
 * Koin module for use cases
 * Contains all business logic use cases
 */
val useCaseModule = module {

    // ========================================
    // Authentication Use Cases
    // ========================================
    single { LoginUseCase(get()) }
    single { RegisterUseCase(get()) }
    single { LoginWithOAuthUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { ForgotPasswordUseCase(get()) }

    // ========================================
    // User Use Cases
    // ========================================
    single { GetCurrentUserUseCase(get()) }
    single { UpdateProfileUseCase(get()) }
    single { UpdateLocationUseCase(get()) }
    single { DeleteAccountUseCase(get()) }

    // ========================================
    // Location Use Cases
    // ========================================
    single { GetAllRegionsUseCase(get()) }
    single { GetMunicipalitiesByRegionUseCase(get()) }
    single { SearchMunicipalitiesUseCase(get()) }
    single { GetMunicipalityByCodeUseCase(get()) }  // 🔥 ADD THIS LINE
}