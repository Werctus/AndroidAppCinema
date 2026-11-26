package com.example.cinemaclient.data.api

import com.example.cinemaclient.data.model.*
import retrofit2.http.*

interface CinemaApi {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): UserResponse

    @GET("api/public/movies")
    suspend fun getMovies(): List<MovieDto>

    @GET("api/public/movies/search")
    suspend fun searchMovies(@Query("query") query: String): List<MovieDto>

    @GET("api/public/movies/genres")
    suspend fun getGenres(): List<GenreDto>

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
        @Body request: BookingRequest
    ): BookingResponse

    @POST("api/bookings/{id}/confirm")
    suspend fun confirmBooking(
        @Path("id") bookingId: Long
    ): BookingResponse

    @GET("api/bookings/my")
    suspend fun getMyBookings(): List<BookingResponse>

    @DELETE("api/bookings/{id}")
    suspend fun cancelBooking(
        @Path("id") bookingId: Long
    )

    @GET("api/bookings/{id}/ticket")
    suspend fun getTicket(
        @Path("id") bookingId: Long
    ): TicketInfo
}