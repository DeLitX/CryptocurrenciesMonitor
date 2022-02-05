package com.delitx.cryptocurrenciesmonitor.domain.model

data class CurrencyHistory(
    val currencyCode: String,
    val historyList: List<CurrencyHistorySnapshot>
)
