package com.sociusfit.core.network

object ApiConfig {
    const val BASE_URL_DEV = "http://10.0.2.2:5000/api/"
    const val BASE_URL_STAGING = "https://staging-api.sociusfit.com/api/"
    const val BASE_URL_PROD = "https://api.sociusfit.com/api/"

    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 60L
    const val WRITE_TIMEOUT = 60L
}