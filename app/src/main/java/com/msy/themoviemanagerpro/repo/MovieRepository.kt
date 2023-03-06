package com.msy.themoviemanagerpro.repo

import androidx.lifecycle.LiveData
import com.msy.themoviemanagerpro.api.RetrofitAPI
import com.msy.themoviemanagerpro.database.FavoriteMovie
import com.msy.themoviemanagerpro.database.MovieDao
import com.msy.themoviemanagerpro.database.WatchListMovie
import com.msy.themoviemanagerpro.model.MovieResponse
import com.msy.themoviemanagerpro.util.Resource
import java.util.Locale
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieDao: MovieDao,
    private val retrofitAPI: RetrofitAPI
): MovieRepositoryInterface {

    override suspend fun insertFavoriteMovie(movie: FavoriteMovie) {
        movieDao.insertFavoriteMovie(movie)
    }

    override suspend fun deleteFavoriteMovie(api_id: Int) {
        movieDao.deleteFavoriteMovie(api_id)
    }

    override fun getFavoriteMovies(): LiveData<List<FavoriteMovie>> {
        return movieDao.observeFavoriteMovies()
    }

    override suspend fun checkIsInFavorites(api_id: Int): Boolean {
        return movieDao.checkIsInFavorites(api_id)
    }

    override suspend fun insertWatchListMovie(movie: WatchListMovie) {
        movieDao.insertWatchListMovie(movie)
    }

    override suspend fun deleteWatchListMovie(api_id: Int) {
        movieDao.deleteWatchListMovie(api_id)
    }

    override fun getWatchListMovies(): LiveData<List<WatchListMovie>> {
        return movieDao.observeWatchListMovies()
    }

    override suspend fun checkIsInWatchList(api_id: Int): Boolean {
        return movieDao.checkIsInWatchList(api_id)
    }

    override suspend fun searchMovie(movie: String): Resource<MovieResponse> {
        return try {
            val response = retrofitAPI.movieSearch(movie, Locale.getDefault().language)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success("Success", it)
                } ?: Resource.error("Error", null)
            } else {
                Resource.error("Error", null)
            }
        } catch (e: Exception) {
            Resource.error("No data!", null)
        }
    }

}