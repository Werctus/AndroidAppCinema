package com.example.cinemaclient.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.cinemaclient.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.PREFS_NAME)

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val tokenKey = stringPreferencesKey(Constants.KEY_AUTH_TOKEN)
    private val roleKey = stringPreferencesKey(Constants.KEY_USER_ROLE)
    private val userIdKey = longPreferencesKey("user_id")

    val authToken: Flow<String?> = context.dataStore.data.map { prefs -> prefs[tokenKey] }

    suspend fun saveAuthData(token: String, userId: Long?, role: String?) {
        context.dataStore.edit { prefs ->
            prefs[tokenKey] = token
            userId?.let { prefs[userIdKey] = it }
            role?.let { prefs[roleKey] = it }
        }
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.first()[tokenKey]
    }

    suspend fun getRole(): String? {
        return context.dataStore.data.first()[roleKey]
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
