package com.example.cinemaclient.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.cinemaclient.data.api.CinemaApi
import com.example.cinemaclient.data.model.AuthResponse
import com.example.cinemaclient.data.model.LoginRequest
import com.example.cinemaclient.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Extension property для создания DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.PREFS_NAME)

@Singleton
class AuthRepository @Inject constructor(
    private val api: CinemaApi,
    @ApplicationContext private val context: Context
) {
    private val tokenKey = stringPreferencesKey(Constants.KEY_AUTH_TOKEN)
    private val roleKey = stringPreferencesKey(Constants.KEY_USER_ROLE)

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))

            // Сохраняем токен и роль локально
            context.dataStore.edit { prefs ->
                prefs[tokenKey] = response.token
                response.role?.let { prefs[roleKey] = it }
            }

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Получаем поток токена для реактивного обновления UI
    val authToken: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[tokenKey]
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.first()[tokenKey]
    }

    suspend fun logout() {
        context.dataStore.edit { it.clear() }
    }
}