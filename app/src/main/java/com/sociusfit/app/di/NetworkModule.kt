package com.sociusfit.app.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sociusfit.app.data.remote.adapter.LocalDateTimeAdapter
import com.sociusfit.app.data.remote.api.AuthApiService
import com.sociusfit.app.data.remote.api.UserApiService
import com.sociusfit.app.data.remote.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

/**
 * Koin module for network dependencies
 * Provides Retrofit, OkHttp, Gson, and API services
 */
val networkModule = module {

    // Gson instance with custom adapters
    single<Gson> {
        GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .setLenient()
            .create()
    }

    // Logging Interceptor
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // Auth Interceptor
    single {
        AuthInterceptor(dataStoreManager = get())
    }

    // OkHttp Client
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit instance
    single {
        Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    // Auth API Service
    single<AuthApiService> {
        get<Retrofit>().create(AuthApiService::class.java)
    }

    // User API Service
    single<UserApiService> {
        get<Retrofit>().create(UserApiService::class.java)
    }
}

/**
 * Get base URL based on build configuration
 * TODO: Replace with actual production URL
 */
private fun getBaseUrl(): String {
    return "http://10.0.2.2:5000/" // Android emulator localhost
    // Production: return "https://api.sociusfit.com/"
}