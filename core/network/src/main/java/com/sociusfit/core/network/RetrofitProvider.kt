package com.sociusfit.core.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sociusfit.core.network.interceptor.AuthInterceptor
import com.sociusfit.core.network.interceptor.ErrorHandlingInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitProvider {
    private fun createGson(): Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .create()

    fun createOkHttpClient(
        tokenProvider: suspend () -> String?,
        isDebug: Boolean = true
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(ApiConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(ApiConfig.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(ApiConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(tokenProvider))
            .addInterceptor(ErrorHandlingInterceptor())
            .apply {
                if (isDebug) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }
            .build()
    }

    fun createRetrofit(
        okHttpClient: OkHttpClient,
        baseUrl: String = ApiConfig.BASE_URL_DEV
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(createGson()))
            .build()
    }
}