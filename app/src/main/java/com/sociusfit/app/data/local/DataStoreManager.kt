package com.sociusfit.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

/**
 * DataStore manager for local persistence
 * Handles authentication token and user preferences
 */
class DataStoreManager(private val context: Context) {

    companion object {
        private const val DATASTORE_NAME = "sociusfit_preferences"

        // Auth Keys
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ID_KEY = intPreferencesKey("user_id")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_FIRST_NAME_KEY = stringPreferencesKey("user_first_name")
        private val USER_LAST_NAME_KEY = stringPreferencesKey("user_last_name")
        private val IS_LOGGED_IN_KEY = stringPreferencesKey("is_logged_in")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = DATASTORE_NAME
    )

    // ============== AUTH FLOWS ==============

    val authToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[AUTH_TOKEN_KEY]
    }

    val userId: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    val userEmail: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL_KEY]
    }

    val userFirstName: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_FIRST_NAME_KEY]
    }

    val userLastName: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_LAST_NAME_KEY]
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN_KEY]?.toBoolean() ?: false
    }

    val refreshToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN_KEY]
    }

    // ============== AUTH METHODS ==============

    suspend fun saveAuthToken(token: String, refreshToken: String? = null) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_KEY] = token
            preferences[IS_LOGGED_IN_KEY] = "true"
            refreshToken?.let {
                preferences[REFRESH_TOKEN_KEY] = it
            }
            Timber.d("Auth token saved")
        }
    }

    suspend fun saveUserData(
        userId: Int,
        email: String,
        firstName: String,
        lastName: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[USER_EMAIL_KEY] = email
            preferences[USER_FIRST_NAME_KEY] = firstName
            preferences[USER_LAST_NAME_KEY] = lastName
        }
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            Timber.d("Refresh token saved")
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun clearAuthToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN_KEY)
            preferences[IS_LOGGED_IN_KEY] = "false"
        }
    }

    suspend fun getAuthToken(): String? {
        var token: String? = null
        context.dataStore.edit { preferences ->
            token = preferences[AUTH_TOKEN_KEY]
        }
        return token
    }
}