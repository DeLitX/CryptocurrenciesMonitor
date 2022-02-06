package com.delitx.cryptocurrenciesmonitor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.delitx.cryptocurrenciesmonitor.R
import com.delitx.cryptocurrenciesmonitor.common.DataState
import com.delitx.cryptocurrenciesmonitor.domain.model.CurrencyHistory
import com.delitx.cryptocurrenciesmonitor.ui.candle_chart.Candle
import com.delitx.cryptocurrenciesmonitor.ui.candle_chart.MarketChart
import com.delitx.cryptocurrenciesmonitor.viewmodels.CryptocurrencyDetailsViewModel

@Composable
fun CryptocurrencyDetails(
    viewModel: CryptocurrencyDetailsViewModel
) {

    IntervalSelector(viewModel = viewModel)
}

@Composable
fun IntervalSelector(
    viewModel: CryptocurrencyDetailsViewModel
) {
    val selectedInterval = viewModel.selectedInterval.collectAsState()
    key(selectedInterval.value) {
        val historyState =
            viewModel.currencyHistories[selectedInterval.value.toString()]?.collectAsState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(15.dp)
        ) {
            historyState?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f),
                    contentAlignment = Alignment.Center,
                ) {
                    when (val state = it.value) {
                        is DataState.Loading -> {
                            CircularProgressIndicator()
                        }
                        is DataState.Failure -> {
                            Text(text = stringResource(id = R.string.failed_to_load_chart))
                        }
                        is DataState.Data -> {
                            MarketChart(
                                candles = state.data.toCandlesList(),
                            )
                        }
                    }
                }
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    CryptocurrencyDetailsViewModel.possibleSelectedIntervals,
                    key = { it.inWholeMilliseconds }
                ) { interval ->
                    IntervalSelectButton(
                        text = interval.toString(),
                        selected = interval == selectedInterval.value,
                        onSelect = {
                            viewModel.selectInterval(interval)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun IntervalSelectButton(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        backgroundColor = if (selected) Color.Gray else MaterialTheme.colors.surface,
        modifier = modifier.then(
            Modifier.clickable {
                if (!selected) {
                    onSelect()
                }
            }
        )
    ) {
        Text(text = text, Modifier.padding(7.dp))
    }
}

fun CurrencyHistory.toCandlesList(): List<Candle> = historyList.map {
    Candle(
        time = it.closeTime,
        open = it.openPrice.toFloat(),
        close = it.closePrice.toFloat(),
        high = it.highPrice.toFloat(),
        low = it.lowPrice.toFloat(),
    )
}
