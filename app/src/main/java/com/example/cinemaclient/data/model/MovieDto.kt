package com.example.cinemaclient.data.model

data class MovieDto(
    val id: Long,
    val title: String,
    val duration: Int,
    val ageRating: String?,
    val posterUrl: String?,
    val genres: List<String>?
)