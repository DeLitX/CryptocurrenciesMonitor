package com.delitx.cryptocurrenciesmonitor.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.CurrenciesList) {
        composable(route = Routes.CurrenciesList) {
            CryptocurrenciesList(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
        composable(
            route = Routes.CurrencyDetails,
            arguments = listOf(
                navArgument(Routes.RouteParts.CurrencyCode) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            CryptocurrencyDetails(
                viewModel = hiltViewModel(
                    backStackEntry
                )
            )
        }
    }
}
