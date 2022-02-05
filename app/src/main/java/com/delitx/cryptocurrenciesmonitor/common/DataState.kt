package com.delitx.cryptocurrenciesmonitor.common

sealed class DataState<T> {
    data class Data<T>(val data: T) : DataState<T>()
    class Failure<T>() : DataState<T>()
    class Loading<T>() : DataState<T>()
}
