package com.example.cinemaclient.data.model

data class BookingRequest(
    val sessionId: Long,
    val seatIds: List<Long>
)

data class BookingResponse(
    val id: Long,
    val status: String,
    val createdAt: String? = null,
    val totalAmount: Double,
    val movieTitle: String? = null,
    val hallName: String? = null,
    val sessionStartTime: String? = null,
    val seats: List<SeatInfo>,
    val ticket: TicketInfo? = null
)

data class SeatInfo(
    val rowNumber: Int,
    val seatNumber: Int,
    val price: Double
)

data class TicketInfo(
    val id: Long,
    val ticketNumber: String,
    val qrCode: String,
    val createdAt: String? = null
)