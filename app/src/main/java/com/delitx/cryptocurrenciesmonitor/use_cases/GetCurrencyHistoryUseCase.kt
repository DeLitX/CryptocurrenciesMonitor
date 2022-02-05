package com.delitx.cryptocurrenciesmonitor.use_cases

import com.delitx.cryptocurrenciesmonitor.domain.model.CurrencyHistory
import com.delitx.cryptocurrenciesmonitor.domain.repository.CryptocurrenciesRepository
import kotlin.time.Duration

class GetCurrencyHistoryUseCase(private val _repository: CryptocurrenciesRepository) {
    suspend operator fun invoke(currencyCode: String, interval: Duration): CurrencyHistory =
        _repository.getCurrencyHistory(currencyCode, interval)
}
