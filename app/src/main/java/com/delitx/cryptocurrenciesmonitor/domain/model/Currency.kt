package com.delitx.cryptocurrenciesmonitor.domain.model

data class Currency(
    val code: String,
    val convertTo: String,
    val price: Double,
) {
    val codeForConversion: String
        get() = code + convertTo
}
