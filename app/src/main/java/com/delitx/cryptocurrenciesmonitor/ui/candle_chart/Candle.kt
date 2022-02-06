package com.delitx.cryptocurrenciesmonitor.ui.candle_chart

data class Candle(
    val time: Long,
    val open: Float,
    val close: Float,
    val high: Float,
    val low: Float
) : Comparable<Candle> {

    override fun compareTo(other: Candle) = if (time < other.time) -1 else 1
}
