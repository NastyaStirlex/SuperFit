package com.nastirlex.superfit.di

import com.nastirlex.superfit.net.repository.AuthRepository
import com.nastirlex.superfit.net.repositoryImpl.AuthRepositoryImpl
import com.nastirlex.superfit.net.service.AuthService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun bindAuthRepositoryImpl(authService: AuthService): AuthRepositoryImpl {
        return AuthRepositoryImpl(authService)
    }
}