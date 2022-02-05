package com.delitx.cryptocurrenciesmonitor.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
