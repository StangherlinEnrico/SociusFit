package com.sociusfit.feature.user.di

import com.sociusfit.feature.user.data.local.TokenDataSource
import com.sociusfit.feature.user.data.remote.api.AuthApiService
import com.sociusfit.feature.user.data.remote.api.UserApiService
import com.sociusfit.feature.user.data.repository.AuthRepositoryImpl
import com.sociusfit.feature.user.data.repository.UserRepositoryImpl
import com.sociusfit.feature.user.domain.repository.AuthRepository
import com.sociusfit.feature.user.domain.repository.UserRepository
import com.sociusfit.feature.user.domain.usecase.auth.GetCurrentUserUseCase
import com.sociusfit.feature.user.domain.usecase.auth.LoginUseCase
import com.sociusfit.feature.user.domain.usecase.auth.LogoutUseCase
import com.sociusfit.feature.user.domain.usecase.auth.RegisterUseCase
import com.sociusfit.feature.user.domain.usecase.user.UpdateProfileUseCase
import com.sociusfit.feature.user.presentation.login.LoginViewModel
import com.sociusfit.feature.user.presentation.profile.ProfileViewModel
import com.sociusfit.feature.user.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val userModule = module {

    single { TokenDataSource(get()) }

    single { get<Retrofit>().create(AuthApiService::class.java) }
    single { get<Retrofit>().create(UserApiService::class.java) }

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }

    factory { RegisterUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { UpdateProfileUseCase(get()) }

    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
}