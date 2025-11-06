package com.sociusfit.app.domain.model

/**
 * Generic wrapper for domain operation results
 * Represents either success or failure with optional message
 */
sealed class Result<out T> {
    /**
     * Successful operation with data
     */
    data class Success<T>(
        val data: T,
        val message: String? = null
    ) : Result<T>()

    /**
     * Failed operation with error message
     */
    data class Error(
        val message: String,
        val exception: Exception? = null
    ) : Result<Nothing>()

    /**
     * Loading state for async operations
     */
    data object Loading : Result<Nothing>()

    /**
     * Check if result is successful
     */
    fun isSuccess(): Boolean = this is Success

    /**
     * Check if result is error
     */
    fun isError(): Boolean = this is Error

    /**
     * Check if result is loading
     */
    fun isLoading(): Boolean = this is Loading

    /**
     * Get data if success, null otherwise
     */
    fun getDataOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    /**
     * Get error message if error, null otherwise
     */
    fun getErrorOrNull(): String? = when (this) {
        is Error -> message
        else -> null
    }
}