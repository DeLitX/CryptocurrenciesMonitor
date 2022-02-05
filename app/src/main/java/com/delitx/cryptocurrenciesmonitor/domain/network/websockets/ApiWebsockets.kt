package com.delitx.cryptocurrenciesmonitor.domain.network.websockets

import com.delitx.cryptocurrenciesmonitor.domain.model.Currency
import kotlinx.coroutines.flow.StateFlow

interface ApiWebsockets {
    fun getCurrenciesPricesLive(currenciesCodes: List<String>): List<StateFlow<Currency>>
    fun stopObservingPrices()
}
