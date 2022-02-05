package com.delitx.cryptocurrenciesmonitor.domain.network.websockets

import androidx.compose.ui.text.toLowerCase
import com.delitx.cryptocurrenciesmonitor.domain.model.Currency
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ApiWebsocketsImpl : ApiWebsockets {
    companion object {
        private const val BASE_URL = "wss://stream.binance.com:9443"
        private const val TRACKER_STREAM_CODE = "miniTicker"
    }

    private val _client: OkHttpClient = OkHttpClient.Builder().build()
    private val _gson = GsonBuilder().create()
    private var _webSocket: WebSocket? = null

    override fun getCurrenciesPricesLive(currenciesCodes: List<String>): List<StateFlow<Currency>> {
        val localList: List<MutableStateFlow<Currency>> = currenciesCodes.map {
            MutableStateFlow(
                Currency(it, Double.NaN)
            )
        }
        val resultList = localList.map { it.asStateFlow() }
        val requestUrl = "$BASE_URL/stream?${currenciesCodes.encodeToMultipleStreams()}"
        val request = Request.Builder()
            .url(requestUrl)
            .build()
        _webSocket = _client.newWebSocket(
            request,
            object : WebSocketListener() {
                override fun onMessage(webSocket: WebSocket, text: String) {
                    val currency = decodeCurrencyFromMessage(text)
                    val observedCurrency = localList.find { it.value.code == currency.code }
                    observedCurrency?.tryEmit(currency)
                }
            }
        )
        return resultList
    }

    override fun stopObservingPrices() {
        _webSocket?.close(1000, "")
        _webSocket = null
    }

    private fun decodeCurrencyFromMessage(message: String): Currency {
        val decodedMessage: WebSocketMessage = _gson.fromJson(message, WebSocketMessage::class.java)
        return Currency(
            code = decodedMessage.data.symbol,
            price = decodedMessage.data.closePrice.toDouble()
        )
    }

    private fun List<String>.encodeToMultipleStreams(
        streamCode: String = TRACKER_STREAM_CODE
    ): String =
        joinToString(separator = "@$streamCode/") { it.lowercase() }

    private class WebSocketMessage(
        val stream: String,
        val data: StreamDataMessage
    )

    private class StreamDataMessage(
        @SerializedName("e")
        val eventType: String,
        @SerializedName("E")
        val eventTime: Double,
        @SerializedName("s")
        val symbol: String,
        @SerializedName("c")
        val closePrice: String,
        @SerializedName("o")
        val openPrice: String,
        @SerializedName("h")
        val highPrice: String,
        @SerializedName("l")
        val lowPrice: String,
        @SerializedName("v")
        val totalTradedBase: String,
        @SerializedName("q")
        val totalTradedQuote: String,
    )
}
