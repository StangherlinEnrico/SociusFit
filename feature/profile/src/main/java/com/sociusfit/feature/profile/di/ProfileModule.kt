package com.sociusfit.feature.profile.di

import com.sociusfit.feature.profile.data.mapper.ProfileMapper
import com.sociusfit.feature.profile.data.mapper.SportsMapper
import com.sociusfit.feature.profile.data.repository.ProfileRepositoryImpl
import com.sociusfit.feature.profile.data.repository.SportsRepositoryImpl
import com.sociusfit.feature.profile.domain.repository.ProfileRepository
import com.sociusfit.feature.profile.domain.repository.SportsRepository
import com.sociusfit.feature.profile.domain.usecase.*
import com.sociusfit.feature.profile.presentation.edit.EditProfileViewModel
import com.sociusfit.feature.profile.presentation.onboarding.bio.OnboardingBioViewModel
import com.sociusfit.feature.profile.presentation.onboarding.photo.OnboardingPhotoViewModel
import com.sociusfit.feature.profile.presentation.onboarding.sports.OnboardingSportsViewModel
import com.sociusfit.feature.profile.presentation.other.OtherUserProfileViewModel
import com.sociusfit.feature.profile.presentation.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module per Profile feature
 */
val profileModule = module {

    // Mappers
    single { ProfileMapper() }
    single { SportsMapper() }

    // Repositories
    single<ProfileRepository> {
        ProfileRepositoryImpl(
            apiService = get(),
            profileDao = get(),
            mapper = get()
        )
    }

    single<SportsRepository> {
        SportsRepositoryImpl(
            apiService = get(),
            sportDao = get(),
            mapper = get()
        )
    }

    // Use Cases
    factory { CreateProfileUseCase(get()) }
    factory { UpdateProfileUseCase(get()) }
    factory { GetMyProfileUseCase(get()) }
    factory { GetProfileByUserIdUseCase(get()) }
    factory { UploadPhotoUseCase(get()) }
    factory { GetAllSportsUseCase(get()) }

    // ViewModels
    viewModel { OnboardingBioViewModel() }
    viewModel { OnboardingSportsViewModel(get()) }
    viewModel { OnboardingPhotoViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { EditProfileViewModel(get(), get(), get(), get()) }
    viewModel { OtherUserProfileViewModel(get()) }
}