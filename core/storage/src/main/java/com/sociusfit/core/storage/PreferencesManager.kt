package com.sociusfit.core.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sociusfit_prefs")

class PreferencesManager(private val context: Context) {

    object Keys {
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val USER_ID = stringPreferencesKey("user_id")
        val MAX_DISTANCE = intPreferencesKey("max_distance")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    }

    suspend fun saveToken(token: String) = withContext(Dispatchers.IO) {
        context.dataStore.edit { prefs ->
            prefs[Keys.AUTH_TOKEN] = token
        }
    }

    suspend fun getToken(): String? = withContext(Dispatchers.IO) {
        context.dataStore.data.map { prefs ->
            prefs[Keys.AUTH_TOKEN]
        }.first()
    }

    fun getTokenFlow(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[Keys.AUTH_TOKEN]
        }
    }

    suspend fun saveUserId(userId: String) = withContext(Dispatchers.IO) {
        context.dataStore.edit { prefs ->
            prefs[Keys.USER_ID] = userId
        }
    }

    suspend fun getUserId(): String? = withContext(Dispatchers.IO) {
        context.dataStore.data.map { prefs ->
            prefs[Keys.USER_ID]
        }.first()
    }

    suspend fun clearAuth() = withContext(Dispatchers.IO) {
        context.dataStore.edit { prefs ->
            prefs.remove(Keys.AUTH_TOKEN)
            prefs.remove(Keys.USER_ID)
        }
    }

    suspend fun saveMaxDistance(distance: Int) = withContext(Dispatchers.IO) {
        context.dataStore.edit { prefs ->
            prefs[Keys.MAX_DISTANCE] = distance
        }
    }

    fun getMaxDistanceFlow(): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            prefs[Keys.MAX_DISTANCE] ?: 50
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) = withContext(Dispatchers.IO) {
        context.dataStore.edit { prefs ->
            prefs[Keys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    fun getNotificationsEnabledFlow(): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[Keys.NOTIFICATIONS_ENABLED] ?: true
        }
    }
}