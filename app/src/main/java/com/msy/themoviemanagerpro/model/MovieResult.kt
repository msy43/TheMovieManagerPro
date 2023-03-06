package com.msy.themoviemanagerpro.model

data class MovieResult(

    val backdrop_path: String?,
    val poster_path: String?,
    val id: Int?,
    val original_language: String?,
    val original_title: String?,
    val title: String?,
    val release_date: String?,
    val overview: String?,
    val popularity: Number?,
    val vote_average: Number?,
    val vote_count: Int?
)