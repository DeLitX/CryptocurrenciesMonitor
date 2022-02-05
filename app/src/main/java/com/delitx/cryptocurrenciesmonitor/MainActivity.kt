package com.delitx.cryptocurrenciesmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.delitx.cryptocurrenciesmonitor.ui.CryptocurrenciesList
import com.delitx.cryptocurrenciesmonitor.ui.CryptocurrencyDetails
import com.delitx.cryptocurrenciesmonitor.ui.Routes
import com.delitx.cryptocurrenciesmonitor.ui.theme.CryptocurrenciesMonitorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptocurrenciesMonitorTheme {
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
        }
    }
}
