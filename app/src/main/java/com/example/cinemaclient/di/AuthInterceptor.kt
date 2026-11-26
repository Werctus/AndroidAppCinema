package com.example.cinemaclient.di

import com.example.cinemaclient.data.local.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val path = originalRequest.url.encodedPath

        // Не добавляем токен на публичные эндпоинты и эндпоинты авторизации
        if (path.startsWith("/api/auth/") || path.startsWith("/api/public/")) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking { tokenManager.getToken() }
        val requestBuilder = originalRequest.newBuilder()

        if (!token.isNullOrBlank()) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
