package com.example.cinemaclient.data.api

import com.example.cinemaclient.data.model.*
import retrofit2.http.*

interface CinemaApi {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("api/public/movies")
    suspend fun getMovies(): List<MovieDto>

    @GET("api/public/movies/{id}/sessions")
    suspend fun getSessions(
        @Path("id") movieId: Long,
        @Query("date") date: String
    ): List<SessionDto>

    @GET("api/public/sessions/{id}/seats")
    suspend fun getSessionSeats(
        @Path("id") sessionId: Long
    ): SessionSeatsResponse

    @POST("api/bookings")
    suspend fun createBooking(
        @Header("Authorization") token: String,
        @Body request: BookingRequest
    ): BookingResponse
}