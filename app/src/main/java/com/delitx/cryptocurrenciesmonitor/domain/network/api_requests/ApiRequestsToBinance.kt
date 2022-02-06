package com.delitx.cryptocurrenciesmonitor.domain.network.api_requests

import com.delitx.cryptocurrenciesmonitor.domain.model.CurrencyHistory
import com.delitx.cryptocurrenciesmonitor.domain.model.CurrencyHistorySnapshot
import java.text.DateFormat
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class ApiRequestsToBinance(
    private val _binanceRequests: BinanceRequests
) : ApiRequests {
    companion object {
        private const val BINANCE_RESPONSE_ITEMS_LIMIT = 500
        private val possibleIntervals = listOf(
            1.minutes,
            3.minutes,
            5.minutes,
            15.minutes,
            30.minutes,
            1.hours,
            2.hours,
            4.hours,
            6.hours,
            8.hours,
            12.hours,
            1.days,
            3.days,
        )
    }

    override suspend fun getCurrencyHistory(
        currencyCode: String,
        intervalOfHistory: Duration
    ): CurrencyHistory {
        val currentTime = System.currentTimeMillis()
        val interval = calculateSuitableBinanceInterval(intervalOfHistory).toString()
        val binanceResponse = _binanceRequests.getCurrencyHistory(
            currencyCode = currencyCode,
            interval = interval,
            startTime = currentTime - intervalOfHistory.inWholeMilliseconds,
            endTime = currentTime
        )
        val historyList = decodeBinanceResponse(binanceResponse)
        return CurrencyHistory(
            currencyCode = currencyCode,
            historyList = historyList,
        )
    }

    private fun decodeBinanceResponse(response: List<List<Any>>): List<CurrencyHistorySnapshot> =
        response.map { item ->
            CurrencyHistorySnapshot(
                openTime = item[0] as Long,
                closeTime = item[6] as Long,
                openPrice = (item[1] as String).toDouble(),
                closePrice = (item[4] as String).toDouble(),
                highPrice = (item[2] as String).toDouble(),
                lowPrice = (item[3] as String).toDouble(),
            )
        }

    private fun getCurrentTime(): Long {
        val dateFormat = DateFormat.getTimeInstance()
        dateFormat.timeZone = TimeZone.getTimeZone("gmt")
        return dateFormat.format(Date()).toLong()
    }

    private fun calculateSuitableBinanceInterval(
        intervalOfHistory: Duration,
        responseItemsLimit: Int = BINANCE_RESPONSE_ITEMS_LIMIT,
    ): Duration {
        val minimalInterval = intervalOfHistory / responseItemsLimit
        var bestInterval: Duration? = null
        for (i in possibleIntervals) {
            if (minimalInterval < i) {
                bestInterval = i
                break
            }
        }
        return bestInterval ?: possibleIntervals.last()
    }
}
