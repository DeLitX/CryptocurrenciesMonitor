package com.delitx.cryptocurrenciesmonitor.viewmodels

import androidx.lifecycle.ViewModel
import com.delitx.cryptocurrenciesmonitor.domain.model.Currency
import com.delitx.cryptocurrenciesmonitor.use_cases.GetCurrenciesListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CryptocurrenciesListViewModel @Inject constructor(
    private val _getCurrenciesListUseCase: GetCurrenciesListUseCase
) : ViewModel() {
    val currenciesList: StateFlow<List<StateFlow<Currency>>> =
        _getCurrenciesListUseCase.currenciesList
}
