package com.delitx.cryptocurrenciesmonitor.ui.candle_chart

import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MarketChart(
    candles: List<Candle>,
    modifier: Modifier = Modifier,
    visibleCandlesCount: Int = candles.size
) {

    val state =
        rememberSaveable(saver = MarketChartState.Saver) {
            MarketChartState.getState(
                candles,
                visibleCandlesCount
            )
        }

    val decimalFormat = DecimalFormat("#0.0##")
    val formatter = SimpleDateFormat("HH:mm dd/MM", Locale.getDefault())
    val bounds = Rect()
    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = 35.sp.value
        color = Color.White.toArgb()
    }

    BoxWithConstraints(modifier = modifier) {
        val chartWidth = constraints.maxWidth - 128.dp.value
        val chartHeight = constraints.maxHeight - 64.dp.value

        state.setViewSize(chartWidth, chartHeight)
        state.calculateGridWidth()

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .scrollable(state.scrollableState, Orientation.Horizontal)
                .transformable(state.transformableState)
        ) {
            drawLine(
                color = Color.White,
                strokeWidth = 2.dp.value,
                start = Offset(0f, chartHeight),
                end = Offset(chartWidth, chartHeight)
            )

            drawLine(
                color = Color.White,
                strokeWidth = 2.dp.value,
                start = Offset(chartWidth, 0f),
                end = Offset(chartWidth, chartHeight)
            )

            drawTimeLines(
                state,
                chartHeight,
                chartWidth,
                bounds,
                textPaint,
                formatter
            )

            drawPriceLines(
                state,
                chartWidth,
                bounds,
                textPaint,
                decimalFormat,
            )

            drawCandles(state)
        }
    }
}

fun DrawScope.drawTimeLines(
    state: MarketChartState,
    chartHeight: Float,
    chartWidth: Float,
    bounds: Rect,
    textPaint: NativePaint,
    formatter: SimpleDateFormat,
) {
    state.timeLines.forEach { candle ->
        val offset = state.xOffset(candle)
        if (offset !in 0f..chartWidth) return@forEach
        drawLine(
            color = Color.White,
            strokeWidth = 1.dp.value,
            start = Offset(offset, 0f),
            end = Offset(offset, chartHeight),
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(10f, 20f),
                phase = 5f
            )
        )
        drawIntoCanvas {
            val text = formatter.format(Date(candle.time))
            textPaint.getTextBounds(text, 0, text.length, bounds)
            val textHeight = bounds.height()
            val textWidth = bounds.width()
            it.nativeCanvas.drawText(
                text,
                if (offset - textWidth / 2 >= 0) {
                    offset - textWidth / 2
                } else {
                    0f
                },
                chartHeight + 8.dp.value + textHeight,
                textPaint
            )
        }
    }
}

fun DrawScope.drawPriceLines(
    state: MarketChartState,
    chartWidth: Float,
    bounds: Rect,
    textPaint: NativePaint,
    decimalFormat: DecimalFormat
) {
    state.priceLines.forEach { value: Float ->
        val yOffset = state.yOffset(value)
        val text = decimalFormat.format(value)
        drawLine(
            color = Color.White,
            strokeWidth = 1.dp.value,
            start = Offset(0f, yOffset),
            end = Offset(chartWidth, yOffset),
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(10f, 20f),
                phase = 5f
            )
        )
        drawIntoCanvas {
            textPaint.getTextBounds(text, 0, text.length, bounds)
            val textHeight = bounds.height()
            it.nativeCanvas.drawText(
                text,
                chartWidth + 8.dp.value,
                yOffset + textHeight / 2,
                textPaint
            )
        }
    }
}

fun DrawScope.drawCandles(
    state: MarketChartState
) {
    state.visibleCandles.forEach { candle ->
        val xOffset = state.xOffset(candle)
        drawLine(
            color = Color.White,
            strokeWidth = 2.dp.value,
            start = Offset(xOffset, state.yOffset(candle.low)),
            end = Offset(xOffset, state.yOffset(candle.high))
        )
        if (candle.open > candle.close) {
            drawRect(
                color = Color.Red,
                topLeft = Offset(xOffset - 6.dp.value, state.yOffset(candle.open)),
                size = Size(
                    12.dp.value,
                    state.yOffset(candle.close) - state.yOffset(candle.open)
                )
            )
        } else {
            drawRect(
                color = Color.Green,
                topLeft = Offset(xOffset - 6.dp.value, state.yOffset(candle.close)),
                size = Size(
                    12.dp.value,
                    state.yOffset(candle.open) - state.yOffset(candle.close)
                )
            )
        }
    }
}
