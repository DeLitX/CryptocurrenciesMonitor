package com.delitx.cryptocurrenciesmonitor.domain.model

data class CurrencyHistorySnapshot(
    val openTime: Long,
    val closeTime: Long,
    val openPrice: Double,
    val closePrice: Double,
    val lowPrice: Double,
    val highPrice: Double,
)
