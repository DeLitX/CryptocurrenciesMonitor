package com.delitx.cryptocurrenciesmonitor.domain.repository

import com.delitx.cryptocurrenciesmonitor.domain.model.Currency
import com.delitx.cryptocurrenciesmonitor.domain.model.CurrencyHistory
import com.delitx.cryptocurrenciesmonitor.domain.network.api_requests.ApiRequests
import com.delitx.cryptocurrenciesmonitor.domain.network.remote_config_requests.RemoteConfigRequests
import com.delitx.cryptocurrenciesmonitor.domain.network.websockets.ApiWebsockets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration

class CryptocurrenciesRepositoryImpl(
    private val _remoteConfigRequests: RemoteConfigRequests,
    private val _apiRequests: ApiRequests,
    private val _apiWebsockets: ApiWebsockets,
) : CryptocurrenciesRepository {
    private val _currenciesList: MutableStateFlow<List<StateFlow<Currency>>> = MutableStateFlow(
        listOf()
    )
    override val currenciesList: StateFlow<List<StateFlow<Currency>>> =
        _currenciesList.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val currenciesCodesList = _remoteConfigRequests.getCurrenciesCodesList()
            val currenciesPricesListLive =
                _apiWebsockets.getCurrenciesPricesLive(currenciesCodesList)
            _currenciesList.emit(currenciesPricesListLive)
        }
    }

    override suspend fun getCurrencyHistory(
        currencyCode: String,
        interval: Duration
    ): CurrencyHistory = _apiRequests.getCurrencyHistory(currencyCode, interval)
}
