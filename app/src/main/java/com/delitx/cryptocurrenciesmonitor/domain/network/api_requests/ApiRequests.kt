package com.delitx.cryptocurrenciesmonitor.domain.network.api_requests

import com.delitx.cryptocurrenciesmonitor.domain.model.CurrencyHistory
import kotlin.time.Duration

interface ApiRequests {
    suspend fun getCurrencyHistory(
        currencyCode: String,
        intervalOfHistory: Duration
    ): CurrencyHistory
}
