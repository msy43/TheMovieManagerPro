package com.msy.themoviemanagerpro.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteMovies")
data class FavoriteMovie(
    var backdrop_path: String,
    var poster_path: String,
    var api_id: Int,
    val original_language: String,
    val original_title: String,
    var title: String,
    var release_date: String,
    var overview: String,
    var popularity: String,
    var vote_average: String,
    var vote_count: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)