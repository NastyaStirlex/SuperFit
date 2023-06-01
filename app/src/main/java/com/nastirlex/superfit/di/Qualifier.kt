package com.nastirlex.superfit.di

import javax.inject.Qualifier

@Qualifier
annotation class RefreshOkHttpClient

@Qualifier
annotation class CommonOkHttpClient

@Qualifier
annotation class SimpleOkHttpClient

@Qualifier
annotation class RefreshRetrofit

@Qualifier
annotation class CommonRetrofit

@Qualifier
annotation class SimpleRetrofit