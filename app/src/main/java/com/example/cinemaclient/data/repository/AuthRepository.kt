package com.example.cinemaclient.data.repository

import com.example.cinemaclient.data.api.CinemaApi
import com.example.cinemaclient.data.local.TokenManager
import com.example.cinemaclient.data.model.AuthResponse
import com.example.cinemaclient.data.model.LoginRequest
import com.example.cinemaclient.data.model.RegisterRequest
import com.example.cinemaclient.data.model.UserResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: CinemaApi,
    private val tokenManager: TokenManager
) {
    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            tokenManager.saveAuthData(response.token, response.userId, response.role)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        email: String,
        password: String,
        firstName: String?,
        lastName: String?
    ): Result<UserResponse> {
        return try {
            val response = api.register(RegisterRequest(email, password, firstName, lastName))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    val authToken: Flow<String?> = tokenManager.authToken

    suspend fun getToken(): String? = tokenManager.getToken()

    suspend fun logout() {
        tokenManager.clear()
    }
}
