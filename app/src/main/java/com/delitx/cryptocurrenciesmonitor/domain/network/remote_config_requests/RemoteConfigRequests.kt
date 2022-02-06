package com.delitx.cryptocurrenciesmonitor.domain.network.remote_config_requests

import com.delitx.cryptocurrenciesmonitor.domain.model.ConversionHolder

interface RemoteConfigRequests {
    suspend fun getCurrenciesCodesList(): List<ConversionHolder>
}
