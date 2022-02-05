package com.delitx.cryptocurrenciesmonitor.di

import com.delitx.cryptocurrenciesmonitor.domain.network.api_requests.ApiRequests
import com.delitx.cryptocurrenciesmonitor.domain.network.remote_config_requests.RemoteConfigRequestsImpl
import com.delitx.cryptocurrenciesmonitor.domain.network.websockets.ApiWebsocketsImpl
import com.delitx.cryptocurrenciesmonitor.domain.repository.CryptocurrenciesRepository
import com.delitx.cryptocurrenciesmonitor.domain.repository.CryptocurrenciesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsersRepositoryModule {
    @Singleton
    @Provides
    fun provideUsersRepository(
        apiRequests: ApiRequests,
    ): CryptocurrenciesRepository = CryptocurrenciesRepositoryImpl(
        _remoteConfigRequests = RemoteConfigRequestsImpl(),
        _apiRequests = apiRequests,
        _apiWebsockets = ApiWebsocketsImpl()
    )
}
