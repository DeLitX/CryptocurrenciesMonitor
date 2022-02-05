package com.delitx.cryptocurrenciesmonitor.ui

import com.delitx.cryptocurrenciesmonitor.ui.Routes.RouteParts.CurrencyCode

object Routes {
    object RouteParts {
        const val CurrencyCode: String = "currencyCode"
        const val CurrencyDetails: String = "currencyDetails"
    }

    const val CurrenciesList: String = "currenciesList"
    const val CurrencyDetails: String = "${RouteParts.CurrencyDetails}/{$CurrencyCode}"

    fun CurrencyDetails(currencyCode: String): String =
        "${RouteParts.CurrencyDetails}/$currencyCode"
}
