package com.example.cinemaclient.data.repository

import com.example.cinemaclient.data.api.CinemaApi
import com.example.cinemaclient.data.model.GenreDto
import com.example.cinemaclient.data.model.MovieDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val api: CinemaApi
) {
    suspend fun getMovies(): Result<List<MovieDto>> {
        return try {
            Result.success(api.getMovies())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchMovies(query: String): Result<List<MovieDto>> {
        return try {
            Result.success(api.searchMovies(query))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGenres(): Result<List<GenreDto>> {
        return try {
            Result.success(api.getGenres())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
