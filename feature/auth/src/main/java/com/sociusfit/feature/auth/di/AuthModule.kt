package com.sociusfit.feature.auth.di

import com.sociusfit.feature.auth.data.repository.AuthRepositoryImpl
import com.sociusfit.feature.auth.domain.repository.AuthRepository
import com.sociusfit.feature.auth.domain.usecase.AutoLoginUseCase
import com.sociusfit.feature.auth.domain.usecase.LoginUseCase
import com.sociusfit.feature.auth.domain.usecase.LogoutUseCase
import com.sociusfit.feature.auth.domain.usecase.RegisterUseCase
import com.sociusfit.feature.auth.domain.usecase.ValidateEmailUseCase
import com.sociusfit.feature.auth.domain.usecase.ValidatePasswordUseCase
import com.sociusfit.feature.auth.presentation.login.LoginViewModel
import com.sociusfit.feature.auth.presentation.register.RegisterViewModel
import com.sociusfit.feature.auth.presentation.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Auth Module - Koin Dependency Injection
 *
 * Definisce le dipendenze del modulo Auth.
 * Organizzato in sezioni: Repository, Use Cases, ViewModels.
 */
val authModule = module {

    // ========== REPOSITORY ==========

    /**
     * AuthRepository
     *
     * Single instance del repository di autenticazione.
     * Dipende da: AuthApiService, PreferencesManager, UserDao
     */
    single<AuthRepository> {
        AuthRepositoryImpl(
            apiService = get(),
            preferencesManager = get(),
            userDao = get()
        )
    }

    // ========== USE CASES ==========

    /**
     * RegisterUseCase
     *
     * Factory instance per gestire la registrazione.
     * Dipende da: AuthRepository
     */
    factory {
        RegisterUseCase(
            authRepository = get()
        )
    }

    /**
     * LoginUseCase
     *
     * Factory instance per gestire il login.
     * Dipende da: AuthRepository
     */
    factory {
        LoginUseCase(
            authRepository = get()
        )
    }

    /**
     * AutoLoginUseCase
     *
     * Factory instance per gestire l'auto-login.
     * Dipende da: AuthRepository
     */
    factory {
        AutoLoginUseCase(
            authRepository = get()
        )
    }

    /**
     * LogoutUseCase
     *
     * Factory instance per gestire il logout.
     * Dipende da: AuthRepository
     */
    factory {
        LogoutUseCase(
            authRepository = get()
        )
    }

    /**
     * ValidateEmailUseCase
     *
     * Factory instance per validazione email.
     * Non ha dipendenze.
     */
    factory {
        ValidateEmailUseCase()
    }

    /**
     * ValidatePasswordUseCase
     *
     * Factory instance per validazione password.
     * Non ha dipendenze.
     */
    factory {
        ValidatePasswordUseCase()
    }

    // ========== VIEW MODELS ==========

    /**
     * RegisterViewModel
     *
     * ViewModel per la schermata di registrazione.
     * Dipende da: RegisterUseCase, ValidateEmailUseCase, ValidatePasswordUseCase
     */
    viewModel {
        RegisterViewModel(
            registerUseCase = get(),
            validateEmailUseCase = get(),
            validatePasswordUseCase = get()
        )
    }

    /**
     * LoginViewModel
     *
     * ViewModel per la schermata di login.
     * Dipende da: LoginUseCase, ValidateEmailUseCase
     */
    viewModel {
        LoginViewModel(
            loginUseCase = get(),
            validateEmailUseCase = get()
        )
    }

    /**
     * SplashViewModel
     *
     * ViewModel per la splash screen.
     * Dipende da: AutoLoginUseCase
     */
    viewModel {
        SplashViewModel(
            autoLoginUseCase = get()
        )
    }
}