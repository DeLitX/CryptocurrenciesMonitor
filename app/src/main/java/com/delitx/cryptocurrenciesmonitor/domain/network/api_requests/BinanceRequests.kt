package com.delitx.cryptocurrenciesmonitor.domain.network.api_requests

import retrofit2.http.GET
import retrofit2.http.Query

interface BinanceRequests {
    @GET("klines")
    suspend fun getCurrencyHistory(
        @Query("symbol") currencyCode: String,
        @Query("interval") interval: String,
        @Query("startTime") startTime: Long,
        @Query("endTime") endTime: Long,
    ): List<List<Any>>
}
