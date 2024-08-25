package com.example.yumyum.util

sealed class UiState<out T> {
    data class Success<T>(val data:List<T>):UiState<T>()
    data object Loading:UiState<Nothing>()
    data class Error<T>(val errorMessage:T):UiState<T>()
}