package com.delitx.cryptocurrenciesmonitor.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.delitx.cryptocurrenciesmonitor.ui.theme.CryptocurrenciesMonitorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptocurrenciesMonitorTheme {
                NavGraph()
            }
        }
    }
}
