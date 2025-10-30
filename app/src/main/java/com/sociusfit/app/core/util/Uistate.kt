package com.sociusfit.app.core.util

/**
 * Stato generico per la UI.
 * Utilizzato nei ViewModel per rappresentare lo stato delle operazioni.
 */
sealed class UiState<out T> {
    /**
     * Stato iniziale, nessuna operazione è stata ancora eseguita
     */
    object Idle : UiState<Nothing>()

    /**
     * Operazione in corso
     */
    object Loading : UiState<Nothing>()

    /**
     * Operazione completata con successo
     */
    data class Success<T>(val data: T) : UiState<T>()

    /**
     * Operazione fallita
     */
    data class Error(
        val message: String,
        val exception: Exception? = null
    ) : UiState<Nothing>()

    // Utility properties
    val isLoading: Boolean get() = this is Loading
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isIdle: Boolean get() = this is Idle
}

/**
 * Converte un Result in UiState
 */
fun <T> Result<T>.toUiState(): UiState<T> {
    return when (this) {
        is Result.Success -> UiState.Success(data)
        is Result.Error -> UiState.Error(
            message = message ?: exception.localizedMessage ?: "Errore sconosciuto",
            exception = exception
        )
    }
}