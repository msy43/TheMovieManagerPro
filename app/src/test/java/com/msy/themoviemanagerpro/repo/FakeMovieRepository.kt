package com.msy.themoviemanagerpro.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.msy.themoviemanagerpro.database.FavoriteMovie
import com.msy.themoviemanagerpro.database.WatchListMovie
import com.msy.themoviemanagerpro.model.MovieResponse
import com.msy.themoviemanagerpro.util.Resource

class FakeMovieRepository : MovieRepositoryInterface {

    private val favoriteMovies = mutableListOf<FavoriteMovie>()
    private val favoriteMoviesLiveData = MutableLiveData<List<FavoriteMovie>>(favoriteMovies)

    private val watchListMovies = mutableListOf<WatchListMovie>()
    private val watchListeMoviesLiveData = MutableLiveData<List<WatchListMovie>>(watchListMovies)

    override suspend fun insertFavoriteMovie(movie: FavoriteMovie) {
        favoriteMovies.add(movie)
        refreshFavoriteMovieData()
    }

    override suspend fun deleteFavoriteMovie(api_id: Int) {
        val item = favoriteMovies.find { it.api_id == api_id }
        favoriteMovies.remove(item)
        refreshFavoriteMovieData()
    }

    override fun getFavoriteMovies(): LiveData<List<FavoriteMovie>> {
        return favoriteMoviesLiveData
    }

    override suspend fun checkIsInFavorites(api_id: Int): Boolean {
        return favoriteMovies.find { it.api_id == api_id } != null

    }

    override suspend fun insertWatchListMovie(movie: WatchListMovie) {
        watchListMovies.add(movie)
        refreshWatchListMovieData()
    }

    override suspend fun deleteWatchListMovie(api_id: Int) {
        val item = watchListMovies.find { it.api_id == api_id }
        watchListMovies.remove(item)
        refreshWatchListMovieData()
    }

    override fun getWatchListMovies(): LiveData<List<WatchListMovie>> {
        return watchListeMoviesLiveData
    }

    override suspend fun checkIsInWatchList(api_id: Int): Boolean {
        return watchListMovies.find { it.api_id == api_id } != null
    }

    override suspend fun searchMovie(movie: String): Resource<MovieResponse> {
        return Resource.success("Success", MovieResponse(arrayListOf()))
    }

    private fun refreshFavoriteMovieData() {
        favoriteMoviesLiveData.postValue(favoriteMovies)
    }

    private fun refreshWatchListMovieData() {
        watchListeMoviesLiveData.postValue(watchListMovies)
    }
}