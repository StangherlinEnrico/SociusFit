package com.sociusfit.app.core.util

sealed class UiState<out T> {

    object Idle : UiState<Nothing>()

    object Loading : UiState<Nothing>()

    data class Success<T>(val data: T) : UiState<T>()

    data class Error(
        val message: String,
        val exception: Exception? = null
    ) : UiState<Nothing>()

    val isLoading: Boolean get() = this is Loading
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isIdle: Boolean get() = this is Idle
}

fun <T> Result<T>.toUiState(): UiState<T> {
    return when (this) {
        is Result.Success -> UiState.Success(data)
        is Result.Error -> UiState.Error(
            message = message ?: exception.localizedMessage ?: "Errore sconosciuto",
            exception = exception
        )
    }
}