package com.sociusfit.app.di

import android.os.Build
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
    single<HttpLoggingInterceptor> {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // Auth Interceptor
    single<AuthInterceptor> {
        AuthInterceptor(dataStoreManager = get())
    }

    // OkHttp Client - FIXED: Rimossa dipendenza circolare con TokenRefreshInterceptor
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<AuthInterceptor>())
            // TODO: Re-implementare TokenRefreshInterceptor con lazy initialization
            // per evitare dipendenze circolari
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit instance
    single<Retrofit> {
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
    return if (isEmulator()) {
        "http://10.0.2.2:5000/"      // Emulator
    } else {
        "http://192.168.1.3:5000/"   // Physical device | Bisogna aprire la porta 5000 dal firewall
    }

    // Production: return "https://api.sociusfit.com/"
}

private fun isEmulator(): Boolean {
    return (Build.FINGERPRINT.startsWith("generic")
            || Build.MODEL.contains("Emulator")
            || Build.MANUFACTURER.contains("Genymotion"))
}