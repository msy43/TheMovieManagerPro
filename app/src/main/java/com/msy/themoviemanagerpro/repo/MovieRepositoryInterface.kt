package com.msy.themoviemanagerpro.repo

import androidx.lifecycle.LiveData
import com.msy.themoviemanagerpro.database.FavoriteMovie
import com.msy.themoviemanagerpro.database.WatchListMovie
import com.msy.themoviemanagerpro.model.MovieResponse
import com.msy.themoviemanagerpro.util.Resource

interface MovieRepositoryInterface {
    suspend fun insertFavoriteMovie(movie: FavoriteMovie)

    suspend fun deleteFavoriteMovie(api_id: Int)

    fun getFavoriteMovies(): LiveData<List<FavoriteMovie>>

    suspend fun checkIsInFavorites(api_id: Int): Boolean

    suspend fun insertWatchListMovie(movie: WatchListMovie)

    suspend fun deleteWatchListMovie(api_id: Int)

    fun getWatchListMovies(): LiveData<List<WatchListMovie>>

    suspend fun checkIsInWatchList(api_id: Int): Boolean

    suspend fun searchMovie(movie: String): Resource<MovieResponse>
}