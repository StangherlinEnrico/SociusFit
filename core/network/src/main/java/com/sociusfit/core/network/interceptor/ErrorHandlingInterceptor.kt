package com.sociusfit.core.network.interceptor

import com.sociusfit.core.domain.NetworkException
import com.sociusfit.core.domain.ServerException
import com.sociusfit.core.domain.UnauthorizedException
import com.sociusfit.core.domain.ValidationException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ErrorHandlingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return try {
            val response = chain.proceed(request)

            when (response.code) {
                in 200..299 -> response
                400 -> throw ValidationException("Bad request")
                401 -> throw UnauthorizedException()
                404 -> throw ValidationException("Resource not found")
                in 500..599 -> throw ServerException("Server error: ${response.code}")
                else -> throw NetworkException("HTTP error: ${response.code}")
            }
        } catch (e: IOException) {
            throw NetworkException("Network error: ${e.message}")
        }
    }
}