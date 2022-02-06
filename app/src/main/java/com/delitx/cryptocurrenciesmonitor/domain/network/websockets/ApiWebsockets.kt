package com.delitx.cryptocurrenciesmonitor.domain.network.websockets

import com.delitx.cryptocurrenciesmonitor.domain.model.ConversionHolder
import com.delitx.cryptocurrenciesmonitor.domain.model.Currency
import kotlinx.coroutines.flow.StateFlow

interface ApiWebsockets {
    fun getCurrenciesPricesLive(conversionCodes: List<ConversionHolder>): List<StateFlow<Currency>>
    fun stopObservingPrices()
}
