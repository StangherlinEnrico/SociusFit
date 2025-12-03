package com.sociusfit.app.di

import com.sociusfit.core.network.ApiConfig
import com.sociusfit.core.network.RetrofitProvider
import com.sociusfit.core.network.api.AuthApiService
import com.sociusfit.core.network.api.ChatApiService
import com.sociusfit.core.network.api.DiscoveryApiService
import com.sociusfit.core.network.api.MatchApiService
import com.sociusfit.core.network.api.NotificationsApiService
import com.sociusfit.core.network.api.ProfileApiService
import com.sociusfit.core.network.api.SportsApiService
import com.sociusfit.core.storage.PreferencesManager
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {
    single {
        val prefsManager: PreferencesManager = get()
        RetrofitProvider.createOkHttpClient(
            tokenProvider = { prefsManager.getToken() },
            isDebug = true
        )
    }

    single {
        RetrofitProvider.createRetrofit(
            okHttpClient = get(),
            baseUrl = ApiConfig.BASE_URL_DEV
        )
    }

    // Semplificato con inline reified
    single<AuthApiService> { get<Retrofit>().create(AuthApiService::class.java) }
    single<ProfileApiService> { get<Retrofit>().create(ProfileApiService::class.java) }
    single<SportsApiService> { get<Retrofit>().create(SportsApiService::class.java) }
    single<DiscoveryApiService> { get<Retrofit>().create(DiscoveryApiService::class.java) }
    single<MatchApiService> { get<Retrofit>().create(MatchApiService::class.java) }
    single<ChatApiService> { get<Retrofit>().create(ChatApiService::class.java) }
    single<NotificationsApiService> { get<Retrofit>().create(NotificationsApiService::class.java) }
}