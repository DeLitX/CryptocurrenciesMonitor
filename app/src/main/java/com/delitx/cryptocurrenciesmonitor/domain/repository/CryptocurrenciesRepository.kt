package com.delitx.cryptocurrenciesmonitor.domain.repository

import com.delitx.cryptocurrenciesmonitor.domain.model.Currency
import com.delitx.cryptocurrenciesmonitor.domain.model.CurrencyHistory
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

interface CryptocurrenciesRepository {
    val currenciesList: StateFlow<List<StateFlow<Currency>>>
    suspend fun getCurrencyHistory(currencyCode: String, interval: Duration): CurrencyHistory
}
