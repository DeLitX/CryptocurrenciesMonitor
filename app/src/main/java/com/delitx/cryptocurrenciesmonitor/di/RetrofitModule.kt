package com.delitx.cryptocurrenciesmonitor.di

import com.delitx.cryptocurrenciesmonitor.domain.network.api_requests.BinanceRequests
import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    const val API_LINK = "https://api.binance.com/api/v3/"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_LINK)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                        .create()
                )
            )
            .build()
    }

    @Provides
    fun provideUsersRequests(retrofit: Retrofit): BinanceRequests =
        retrofit.create(BinanceRequests::class.java)
}
