package com.delitx.cryptocurrenciesmonitor.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.delitx.cryptocurrenciesmonitor.domain.model.Currency
import com.delitx.cryptocurrenciesmonitor.viewmodels.CryptocurrenciesListViewModel

@Composable
fun CryptocurrenciesList(
    viewModel: CryptocurrenciesListViewModel,
    navController: NavController,
) {
    val list = viewModel.currenciesList.collectAsState()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(list.value, key = { it.value.codeForConversion }) { item ->
            val currency = item.collectAsState()
            CryptocurrenciesListItem(currency = currency.value, navController = navController)
        }
    }
}

@Composable
fun CryptocurrenciesListItem(
    currency: Currency,
    navController: NavController,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp)
            .clickable {
                navController.navigate(Routes.CurrencyDetails(currency.codeForConversion))
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = currency.code)
            Text(text = currency.price.toString())
        }
    }
}
