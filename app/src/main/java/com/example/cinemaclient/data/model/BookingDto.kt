package com.example.cinemaclient.data.model

data class BookingRequest(
    val sessionId: Long,
    val seatIds: List<Long>
)

data class BookingResponse(
    val id: Long,
    val status: String,
    val totalAmount: Double,
    val seats: List<SeatInfo>
)

data class SeatInfo(
    val rowNumber: Int,
    val seatNumber: Int,
    val price: Double
)