package com.sociusfit.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sociusfit_prefs")

/**
 * 🔥 COMPLETE: DataStore manager con supporto refresh token e isLoggedIn
 */
class DataStoreManager(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")  // 🔥 ADD
        private val USER_ID_KEY = intPreferencesKey("user_id")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_FIRST_NAME_KEY = stringPreferencesKey("user_first_name")
        private val USER_LAST_NAME_KEY = stringPreferencesKey("user_last_name")
    }

    // Auth token flow
    val authToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[AUTH_TOKEN_KEY]
    }

    // Refresh token flow
    val refreshToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN_KEY]
    }

    // 🔥 ADD: Is logged in flow
    val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN_KEY] ?: false
    }

    // User data flows
    val userId: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    val userEmail: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_EMAIL_KEY]
    }

    val userFirstName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_FIRST_NAME_KEY]
    }

    val userLastName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_LAST_NAME_KEY]
    }

    /**
     * Save auth token AND refresh token
     */
    suspend fun saveAuthToken(token: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_KEY] = token
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[IS_LOGGED_IN_KEY] = true  // 🔥 SET LOGGED IN
        }
    }

    /**
     * Update only access token (dopo refresh)
     */
    suspend fun updateAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_KEY] = token
        }
    }

    /**
     * Update only refresh token (se backend ne ritorna uno nuovo)
     */
    suspend fun updateRefreshToken(refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    /**
     * Save user data to DataStore
     */
    suspend fun saveUserData(
        userId: Int,
        email: String,
        firstName: String,
        lastName: String
    ) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[USER_EMAIL_KEY] = email
            preferences[USER_FIRST_NAME_KEY] = firstName
            preferences[USER_LAST_NAME_KEY] = lastName
        }
    }

    /**
     * Clear all data from DataStore (logout)
     */
    suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}