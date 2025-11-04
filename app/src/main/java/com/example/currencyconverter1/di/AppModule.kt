package com.example.currencyconverter1.di

import android.content.Context
import com.example.currencyconverter1.data.repository.ExchangeApi
import com.example.currencyconverter1.data.repository.RatesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideExchangeApi(): ExchangeApi = ExchangeApi()

    @Provides
    @Singleton
    fun provideRatesRepository(
        @ApplicationContext context: Context,
        api: ExchangeApi
    ): RatesRepository = RatesRepository(context, api)
}