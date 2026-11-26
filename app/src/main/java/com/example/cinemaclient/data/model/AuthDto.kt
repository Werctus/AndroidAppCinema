package com.example.cinemaclient.data.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String?,
    val lastName: String?
)

data class AuthResponse(
    val token: String, // JWT токен
    val userId: Long?,
    val role: String?,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null
)

data class UserResponse(
    val id: Long,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val role: String?
)