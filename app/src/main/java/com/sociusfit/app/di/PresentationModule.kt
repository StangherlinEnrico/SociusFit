package com.sociusfit.app.di

import com.sociusfit.app.presentation.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for presentation layer (ViewModels)
 */
val presentationModule = module {

    // Splash ViewModel
    viewModel { SplashViewModel() }
}