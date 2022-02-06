package com.delitx.cryptocurrenciesmonitor.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delitx.cryptocurrenciesmonitor.common.DataState
import com.delitx.cryptocurrenciesmonitor.domain.model.CurrencyHistory
import com.delitx.cryptocurrenciesmonitor.ui.Routes
import com.delitx.cryptocurrenciesmonitor.use_cases.GetCurrencyHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

@HiltViewModel
class CryptocurrencyDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val _getCurrencyHistoryUseCase: GetCurrencyHistoryUseCase
) : ViewModel() {
    private val currencyCode: String = savedStateHandle.get(Routes.RouteParts.CurrencyCode)!!

    companion object {
        val possibleSelectedIntervals = listOf(
            30.days,
            7.days
        )
    }

    private val _currencyHistories: MutableMap<String, StateFlow<DataState<CurrencyHistory>>> =
        mutableMapOf()
    val currencyHistories: Map<String, StateFlow<DataState<CurrencyHistory>>> = _currencyHistories

    private val _selectedInterval: MutableStateFlow<Duration> =
        MutableStateFlow(possibleSelectedIntervals[0])
    val selectedInterval: StateFlow<Duration> = _selectedInterval.asStateFlow()

    init {
        selectInterval(possibleSelectedIntervals[0])
    }

    fun selectInterval(interval: Duration) {
        viewModelScope.launch {
            if (_currencyHistories[interval.toString()] != null) {
                _selectedInterval.emit(interval)
            } else {
                val history: MutableStateFlow<DataState<CurrencyHistory>> =
                    MutableStateFlow(DataState.Loading())
                _currencyHistories[interval.toString()] = history
                _selectedInterval.emit(interval)
                try {
                    val loadedHistory = _getCurrencyHistoryUseCase(currencyCode, interval)
                    history.emit(DataState.Data(loadedHistory))
                } catch (e: Exception) {
                    Log.d("RetrofitException", e.message ?: "")
                    history.emit(DataState.Failure())
                }
            }
        }
    }
}
