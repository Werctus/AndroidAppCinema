package com.example.cinemaclient.data.model

data class MovieDto(
    val id: Long,
    val title: String,
    val duration: Int,
    val ageRating: String?,
    val releaseDate: String? = null,
    val posterUrl: String?,
    val genres: List<String>?
)

data class GenreDto(
    val id: Long,
    val name: String
)