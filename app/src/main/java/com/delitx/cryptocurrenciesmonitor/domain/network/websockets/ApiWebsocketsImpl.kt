package com.delitx.cryptocurrenciesmonitor.domain.network.websockets

import com.delitx.cryptocurrenciesmonitor.domain.model.ConversionHolder
import com.delitx.cryptocurrenciesmonitor.domain.model.Currency
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.*

class ApiWebsocketsImpl : ApiWebsockets {
    companion object {
        private const val BASE_URL = "wss://stream.binance.com:9443"
        private const val TRACKER_STREAM_CODE = "miniTicker"
    }

    private val _client: OkHttpClient = OkHttpClient.Builder().build()
    private val _gson = GsonBuilder().create()
    private var _webSocket: WebSocket? = null

    override fun getCurrenciesPricesLive(conversionCodes: List<ConversionHolder>): List<StateFlow<Currency>> {
        val localList: List<MutableStateFlow<Currency>> = conversionCodes.map {
            MutableStateFlow(
                Currency(it.fromCurrency, it.toCurrency, Double.NaN)
            )
        }
        val resultList = localList.map { it.asStateFlow() }
        val requestUrl = "$BASE_URL/stream?streams=${conversionCodes.encodeToMultipleStreams()}"
        val request = Request.Builder()
            .url(requestUrl)
            .build()
        _webSocket = _client.newWebSocket(
            request,
            object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosing(webSocket, code, reason)
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosed(webSocket, code, reason)
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    super.onFailure(webSocket, t, response)
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    val currency = decodeCurrencyFromMessage(text)
                    val observedCurrency =
                        localList.find { it.value.codeForConversion == currency.symbol }
                    observedCurrency?.tryEmit(
                        observedCurrency.value.copy(
                            price = currency.closePrice.toDouble()
                        )
                    )
                }
            }
        )
        return resultList
    }

    override fun stopObservingPrices() {
        _webSocket?.close(1000, "")
        _webSocket = null
    }

    private fun decodeCurrencyFromMessage(message: String): StreamDataMessage {
        val decodedMessage: WebSocketMessage = _gson.fromJson(message, WebSocketMessage::class.java)
        return decodedMessage.data
    }

    private fun List<ConversionHolder>.encodeToMultipleStreams(
        streamCode: String = TRACKER_STREAM_CODE
    ): String =
        joinToString(
            separator = "@$streamCode/",
            postfix = "@$streamCode"
        ) { it.fromCurrency.lowercase() + it.toCurrency.lowercase() }

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
