package com.delitx.cryptocurrenciesmonitor.domain.network.remote_config_requests

interface RemoteConfigRequests {
    suspend fun getCurrenciesCodesList(): List<String>
}
