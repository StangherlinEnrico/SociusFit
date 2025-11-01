package com.sociusfit.app.di

import com.sociusfit.app.presentation.auth.login.LoginViewModel
import com.sociusfit.app.presentation.auth.register.RegisterViewModel
import com.sociusfit.app.presentation.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
}