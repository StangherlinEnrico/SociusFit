package com.sociusfit.app.di

import com.sociusfit.app.presentation.auth.forgotpassword.ForgotPasswordViewModel
import com.sociusfit.app.presentation.auth.login.LoginViewModel
import com.sociusfit.app.presentation.auth.register.RegisterViewModel
import com.sociusfit.app.presentation.profile.ProfileViewModel
import com.sociusfit.app.presentation.profile.edit.EditProfileViewModel
import com.sociusfit.app.presentation.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for presentation layer (ViewModels)
 */
val presentationModule = module {

    // Splash ViewModel
    viewModel {
        SplashViewModel(
            checkLoginStatusUseCase = get()
        )
    }

    // Login ViewModel
    viewModel {
        LoginViewModel(
            loginUseCase = get(),
            loginWithOAuthUseCase = get()
        )
    }

    // Register ViewModel
    viewModel {
        RegisterViewModel(
            registerUseCase = get(),
            loginWithOAuthUseCase = get()
        )
    }

    // Forgot Password ViewModel
    viewModel {
        ForgotPasswordViewModel(
            forgotPasswordUseCase = get()
        )
    }

    // Profile ViewModel
    viewModel {
        ProfileViewModel(
            getCurrentUserUseCase = get()
        )
    }

    // Edit Profile ViewModel
    viewModel {
        EditProfileViewModel(
            getCurrentUserUseCase = get(),
            updateProfileUseCase = get(),
            searchMunicipalitiesUseCase = get()
        )
    }
}