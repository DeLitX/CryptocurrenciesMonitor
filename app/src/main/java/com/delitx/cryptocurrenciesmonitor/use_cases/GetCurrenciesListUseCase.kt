package com.delitx.cryptocurrenciesmonitor.use_cases

import com.delitx.cryptocurrenciesmonitor.domain.repository.CryptocurrenciesRepository

class GetCurrenciesListUseCase(private val _repository: CryptocurrenciesRepository) {
    val currenciesList = _repository.currenciesList
}
