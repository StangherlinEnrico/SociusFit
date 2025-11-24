package com.sociusfit.app.di

import com.sociusfit.app.domain.usecase.auth.CheckLoginStatusUseCase
import com.sociusfit.app.domain.usecase.auth.ForgotPasswordUseCase
import com.sociusfit.app.domain.usecase.auth.LoginUseCase
import com.sociusfit.app.domain.usecase.auth.LoginWithOAuthUseCase
import com.sociusfit.app.domain.usecase.auth.LogoutUseCase
import com.sociusfit.app.domain.usecase.auth.RegisterUseCase
import com.sociusfit.app.domain.usecase.user.DeleteAccountUseCase
import com.sociusfit.app.domain.usecase.user.GetCurrentUserUseCase
import com.sociusfit.app.domain.usecase.user.UpdateLocationUseCase
import com.sociusfit.app.domain.usecase.user.UpdateProfileUseCase
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
}