package com.sociusfit.app.di

import com.sociusfit.app.AppConfig
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
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 * Network Module - Koin
 *
 * Configura Retrofit, OkHttpClient e tutti gli API Services.
 * Responsabilità:
 * - Configurazione HTTP client con interceptors
 * - Token provider per autenticazione
 * - Creazione di tutti gli API services
 */
val networkModule = module {

    // ========== OK HTTP CLIENT ==========

    /**
     * OkHttpClient configurato con:
     * - Auth interceptor (aggiunge token JWT)
     * - Logging interceptor (solo debug)
     * - Timeout configuration
     */
    single<OkHttpClient> {
        val preferencesManager: PreferencesManager = get()
        RetrofitProvider.createOkHttpClient(
            tokenProvider = { preferencesManager.getToken() },
            isDebug = AppConfig.IS_DEBUG
        )
    }

    // ========== RETROFIT ==========

    /**
     * Retrofit instance configurata con:
     * - Base URL (dev/prod based on AppConfig.IS_DEBUG)
     * - Moshi converter
     * - OkHttpClient custom
     */
    single<Retrofit> {
        RetrofitProvider.createRetrofit(
            okHttpClient = get(),
            baseUrl = AppConfig.BASE_URL
        )
    }

// ========== API SERVICES ==========

/**
 * Auth API Service
 * Endpoints: /api/auth/
*/
single<AuthApiService> {
get<Retrofit>().create(AuthApiService::class.java)
}

/**
 * Profile API Service
 * Endpoints: /api/profiles/
*/
single<ProfileApiService> {
get<Retrofit>().create(ProfileApiService::class.java)
}

/**
 * Sports API Service
 * Endpoints: /api/sports/
*/
single<SportsApiService> {
get<Retrofit>().create(SportsApiService::class.java)
}

/**
 * Discovery API Service
 * Endpoints: /api/discovery/
*/
single<DiscoveryApiService> {
get<Retrofit>().create(DiscoveryApiService::class.java)
}

/**
 * Match API Service
 * Endpoints: /api/matches/
*/
single<MatchApiService> {
get<Retrofit>().create(MatchApiService::class.java)
}

/**
 * Chat API Service
 * Endpoints: /api/chat/
*/
single<ChatApiService> {
get<Retrofit>().create(ChatApiService::class.java)
}

/**
 * Notifications API Service
 * Endpoints: /api/notifications/
*/
single<NotificationsApiService> {
get<Retrofit>().create(NotificationsApiService::class.java)
}
}