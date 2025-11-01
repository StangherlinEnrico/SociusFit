package com.sociusfit.app.di

import com.sociusfit.app.domain.usecase.auth.*
import org.koin.dsl.module

val useCaseModule = module {
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { ValidateTokenUseCase(get()) }
    factory { LogoutUseCase(get()) }
}