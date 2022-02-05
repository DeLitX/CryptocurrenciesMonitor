package com.delitx.cryptocurrenciesmonitor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delitx.cryptocurrenciesmonitor.common.DataState
import com.delitx.cryptocurrenciesmonitor.domain.model.CurrencyHistory
import com.delitx.cryptocurrenciesmonitor.use_cases.GetCurrencyHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

@HiltViewModel
class CryptocurrencyDetailsViewModel(
    private val currencyCode: String,
    private val _getCurrencyHistoryUseCase: GetCurrencyHistoryUseCase
) : ViewModel() {
    private val _currencyHistories: MutableMap<String, StateFlow<DataState<CurrencyHistory>>> =
        mutableMapOf()
    val currencyHistories: Map<String, StateFlow<DataState<CurrencyHistory>>> = _currencyHistories

    private val _selectedInterval: MutableStateFlow<Duration> = MutableStateFlow(30.days)
    val selectedInterval: StateFlow<Duration> = _selectedInterval.asStateFlow()

    init {
        selectInterval(30.days)
    }

    fun selectInterval(interval: Duration) {
        viewModelScope.launch {
            val history: MutableStateFlow<DataState<CurrencyHistory>> =
                MutableStateFlow(DataState.Loading())
            _currencyHistories[interval.toString()] = history
            _selectedInterval.emit(interval)
            try {
                val loadedHistory = _getCurrencyHistoryUseCase(currencyCode, interval)
                history.emit(DataState.Data(loadedHistory))
            } catch (e: Exception) {
                history.emit(DataState.Failure())
            }
        }
    }
}
