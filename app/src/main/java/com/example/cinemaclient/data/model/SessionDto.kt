package com.example.cinemaclient.data.model

data class SessionDto(
    val id: Long,
    val movieTitle: String,
    val hallName: String,
    val startTime: String,
    val price: Double,
    val format: String
)

data class SessionSeatsResponse(
    val sessionId: Long,
    val movieId: Long,
    val movieTitle: String,
    val hallId: Long,
    val hallName: String,
    val startTime: String,
    val basePrice: Double,
    val format: String,
    val totalRows: Int,
    val totalSeats: Int,
    val seats: List<SeatDto>
)

data class SeatDto(
    val id: Long,
    val rowNumber: Int,
    val seatNumber: Int,
    val seatType: String,
    val price: Double,
    val isAvailable: Boolean
)
