package com.sociusfit.app.core

object Constants {

    // API Configuration
    const val BASE_URL = "https://api.sociusfit.com/" // TODO: Aggiornare quando il server è pronto
    const val API_TIMEOUT_SECONDS = 30L

    // DataStore Keys
    const val DATASTORE_NAME = "sociusfit_preferences"
    const val KEY_AUTH_TOKEN = "auth_token"
    const val KEY_REFRESH_TOKEN = "refresh_token"
    const val KEY_USER_ID = "user_id"
    const val KEY_THEME_MODE = "theme_mode"

    // Database
    const val DATABASE_NAME = "sociusfit_database"
    const val DATABASE_VERSION = 1

    // Matching Algorithm Weights
    const val WEIGHT_LEVEL_SIMILARITY = 0.4f
    const val WEIGHT_AVAILABILITY_OVERLAP = 0.3f
    const val WEIGHT_DISTANCE_PROXIMITY = 0.3f

    // Matching Parameters
    const val MAX_LEVEL_DIFFERENCE = 1
    const val DEFAULT_RADIUS_KM = 10
    const val MIN_RADIUS_KM = 5
    const val MAX_RADIUS_KM = 50

    // UI Constants
    const val SWIPE_THRESHOLD = 0.4f
    const val ANIMATION_DURATION_MS = 300
    const val DEBOUNCE_TIME_MS = 300L

    // Pagination
    const val DEFAULT_PAGE_SIZE = 20
    const val INITIAL_PAGE = 1

    // Validation
    const val MIN_PASSWORD_LENGTH = 8
    const val MIN_NAME_LENGTH = 2
    const val MAX_NAME_LENGTH = 50
}