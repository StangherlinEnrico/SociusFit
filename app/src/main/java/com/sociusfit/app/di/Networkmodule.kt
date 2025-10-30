package com.sociusfit.app.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sociusfit.app.core.Constants
import com.sociusfit.app.data.local.datastore.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Modulo Hilt per fornire le dipendenze di rete (Retrofit, OkHttp)
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Fornisce l'istanza di Gson per la serializzazione JSON
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    /**
     * Fornisce l'interceptor per il logging delle richieste HTTP
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    /**
     * Fornisce l'interceptor per aggiungere il token JWT alle richieste
     */
    @Provides
    @Singleton
    fun provideAuthInterceptor(
        dataStoreManager: DataStoreManager
    ): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()

            // Ottiene il token dal DataStore
            val token = runBlocking {
                dataStoreManager.authToken.first()
            }

            // Se c'è un token, lo aggiunge all'header
            val newRequest = if (token != null) {
                originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            } else {
                originalRequest
            }

            chain.proceed(newRequest)
        }
    }

    /**
     * Fornisce l'istanza di OkHttpClient configurata
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(Constants.API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(Constants.API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(Constants.API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Fornisce l'istanza di Retrofit
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // TODO: Aggiungere provide methods per le API interfaces quando saranno create
    // Esempio:
    // @Provides
    // @Singleton
    // fun provideAuthApi(retrofit: Retrofit): AuthApi {
    //     return retrofit.create(AuthApi::class.java)
    // }
}