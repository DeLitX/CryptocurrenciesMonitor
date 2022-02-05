package com.delitx.cryptocurrenciesmonitor.use_cases

import com.delitx.cryptocurrenciesmonitor.domain.repository.CryptocurrenciesRepository
import javax.inject.Inject

class GetCurrenciesListUseCase @Inject constructor(private val _repository: CryptocurrenciesRepository) {
    val currenciesList = _repository.currenciesList
}
