package com.example.cinemaclient.data.model


data class SessionDto(
    val id: Long,
    val movieTitle: String,
    val hallName: String,
    val startTime: String,
    val price: Double,
    val format: String
)