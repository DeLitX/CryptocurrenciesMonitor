package com.delitx.cryptocurrenciesmonitor.domain.network.remote_config_requests

import com.delitx.cryptocurrenciesmonitor.domain.model.ConversionHolder
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RemoteConfigRequestsImpl : RemoteConfigRequests {
    private val _remoteConfig = FirebaseRemoteConfig.getInstance()
    private val _configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 0
    }

    companion object {
        private const val CURRENCIES_CODES_KEY = "currenciesCodes"
        private const val CONVERT_TO_KEY = "convertTo"
    }

    init {
        _remoteConfig.setConfigSettingsAsync(_configSettings)
        _remoteConfig.setDefaultsAsync(
            mapOf(
                CURRENCIES_CODES_KEY to "BTC ETH LTC DOGE",
                CONVERT_TO_KEY to "USDT"
            )
        )
    }

    override suspend fun getCurrenciesCodesList(): List<ConversionHolder> {
        val isSuccess = syncDataFromRemoteConfig()
        val codesList = _remoteConfig.getString(CURRENCIES_CODES_KEY).split(" ")
        val convertTo = _remoteConfig.getString(CONVERT_TO_KEY)
        return codesList.map { ConversionHolder(it, convertTo) }
    }

    private suspend fun syncDataFromRemoteConfig(): Boolean =
        suspendCoroutine { cont ->
            _remoteConfig.fetchAndActivate()
                .addOnCompleteListener {
                    cont.resume(it.isSuccessful)
                }
        }
}
