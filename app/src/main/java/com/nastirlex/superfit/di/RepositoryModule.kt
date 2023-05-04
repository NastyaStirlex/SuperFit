package com.nastirlex.superfit.di

import com.nastirlex.superfit.net.repository.AuthRepository
import com.nastirlex.superfit.net.repositoryImpl.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepositoryImpl(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}