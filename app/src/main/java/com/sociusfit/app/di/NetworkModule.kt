// File: app/src/main/java/com/sociusfit/app/di/NetworkModule.kt

package com.sociusfit.app.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sociusfit.app.data.remote.api.AuthApiService
import com.sociusfit.app.data.remote.api.UserApiService
import com.sociusfit.app.data.remote.interceptor.AuthInterceptor
import com.sociusfit.app.data.remote.interceptor.TokenRefreshInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    // 🔥 ADD: Gson instance
    single<Gson> {
        GsonBuilder()
            .setLenient()
            .create()
    }

    // Logging Interceptor
    single {
        HttpLoggingInterceptor().apply {
            level = BODY
        }
    }

    // Auth Interceptor
    single {
        AuthInterceptor(dataStoreManager = get())
    }

    // 🔥 SEPARATE: OkHttpClient BASE (senza TokenRefreshInterceptor)
    single(named("base")) {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // 🔥 SEPARATE: Retrofit BASE (per AuthApiService)
    single(named("base")) {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.3:5000/")
            .client(get(named("base")))
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    // 🔥 AuthApiService SENZA interceptor auth (per evitare loop)
    single {
        get<Retrofit>(named("base")).create(AuthApiService::class.java)
    }

    // 🔥 Token Refresh Interceptor (NOW can get AuthApiService)
    single {
        TokenRefreshInterceptor(
            dataStoreManager = get(),
            authApiService = get()
        )
    }

    // 🔥 MAIN: OkHttpClient con TUTTI gli interceptor
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<AuthInterceptor>())  // Aggiunge token
            .addInterceptor(get<TokenRefreshInterceptor>())  // Gestisce refresh
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // 🔥 MAIN: Retrofit con OkHttpClient completo
    single {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.3:5000/")
            .client(get<OkHttpClient>())  // Usa quello con tutti gli interceptor
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    // 🔥 UserApiService usa Retrofit MAIN (con auth)
    single {
        get<Retrofit>().create(UserApiService::class.java)
    }
}