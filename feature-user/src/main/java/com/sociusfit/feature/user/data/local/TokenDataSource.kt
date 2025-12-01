package com.sociusfit.feature.user.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class TokenDataSource(private val context: Context) {

    private val tokenKey = stringPreferencesKey("auth_token")

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[tokenKey] = token
        }
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.first()[tokenKey]
    }

    suspend fun clearToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(tokenKey)
        }
    }

    fun isAuthenticated(): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[tokenKey] != null
        }
    }
}