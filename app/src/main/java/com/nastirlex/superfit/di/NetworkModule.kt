package com.nastirlex.superfit.di

import com.nastirlex.superfit.net.AuthorizationInterceptor
import com.nastirlex.superfit.net.RefreshAuthenticator
import com.nastirlex.superfit.net.service.AuthService
import com.nastirlex.superfit.net.service.ProfileService
import com.nastirlex.superfit.net.service.RefreshTokenService
import com.nastirlex.superfit.net.service.TrainingService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @SimpleOkHttpClient
    @Singleton
    @Provides
    fun provideSimpleHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            val logLevel = HttpLoggingInterceptor.Level.BODY
            addInterceptor(HttpLoggingInterceptor().setLevel(logLevel))
        }.build()
    }

    @CommonOkHttpClient
    @Singleton
    @Provides
    fun provideHttpClient(
        authorizationInterceptor: AuthorizationInterceptor,
        refreshAuthenticator: RefreshAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            val logLevel = HttpLoggingInterceptor.Level.BODY
            addInterceptor(HttpLoggingInterceptor().setLevel(logLevel))
            addInterceptor(authorizationInterceptor)
            authenticator(refreshAuthenticator)
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
        }.build()
    }

    @RefreshOkHttpClient
    @Singleton
    @Provides
    fun provideRefreshHttpClient(refreshAuthenticator: RefreshAuthenticator): OkHttpClient {
        return OkHttpClient.Builder().apply {
            val logLevel = HttpLoggingInterceptor.Level.BODY
            addInterceptor(HttpLoggingInterceptor().setLevel(logLevel))
            authenticator(refreshAuthenticator)
        }.build()
    }

    @CommonRetrofit
    @Singleton
    @Provides
    fun provideRetrofit(@CommonOkHttpClient okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://fitness.wsmob.xyz:22169/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @RefreshRetrofit
    @Singleton
    @Provides
    fun provideRefreshRetrofit(@RefreshOkHttpClient okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://fitness.wsmob.xyz:22169/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @SimpleRetrofit
    @Singleton
    @Provides
    fun provideSimpleRetrofit(@SimpleOkHttpClient okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://fitness.wsmob.xyz:22169/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }


    @Singleton
    @Provides
    fun provideAuthService(@RefreshRetrofit retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun provideTrainingService(@CommonRetrofit retrofit: Retrofit): TrainingService =
        retrofit.create(TrainingService::class.java)

    @Singleton
    @Provides
    fun provideRefreshTokenService(@SimpleRetrofit retrofit: Retrofit): RefreshTokenService =
        retrofit.create(RefreshTokenService::class.java)

    @Singleton
    @Provides
    fun provideProfileService(@CommonRetrofit retrofit: Retrofit): ProfileService =
        retrofit.create(ProfileService::class.java)
}