package com.msy.themoviemanagerpro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msy.themoviemanagerpro.database.FavoriteMovie
import com.msy.themoviemanagerpro.database.WatchListMovie
import com.msy.themoviemanagerpro.model.MovieResult
import com.msy.themoviemanagerpro.repo.MovieRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailScreenViewModel @Inject constructor(private val repository: MovieRepositoryInterface) :
    ViewModel() {

    private val movie = MutableLiveData<MovieResult>()
    val movieR: LiveData<MovieResult> get() = movie

    private val inFavorite = MutableLiveData<Boolean>()
    val inFavoriteR: LiveData<Boolean> get() = inFavorite

    private val inWatchList = MutableLiveData<Boolean>()
    val inWatchListR: LiveData<Boolean> get() = inWatchList

    private val watchListMovies = MutableLiveData<List<WatchListMovie>>()
    val watchListMoviesR: LiveData<List<WatchListMovie>> get() = watchListMovies

    private val favoriteMovies = MutableLiveData<List<FavoriteMovie>>()
    val favoriteMoviesR: LiveData<List<FavoriteMovie>> get() = favoriteMovies


    fun insertFavoriteMovie(movie: FavoriteMovie) = viewModelScope.launch {
        repository.insertFavoriteMovie(movie)
        inFavorite.value = true
    }

    fun deleteFavoriteMovie(api_id: Int) = viewModelScope.launch {
        repository.deleteFavoriteMovie(api_id)
        inFavorite.value = false
    }

    private fun insertWatchListMovie(movie: WatchListMovie) = viewModelScope.launch {
        repository.insertWatchListMovie(movie)
        inWatchList.value = true
    }

    fun deleteWatchListMovie(api_id: Int) = viewModelScope.launch {
        repository.deleteWatchListMovie(api_id)
        inWatchList.value = false
    }

    fun checkIsInFavorites() = viewModelScope.launch {
        inFavorite.value = repository.checkIsInFavorites(movieR.value!!.id ?: 0)
    }

    fun checkIsInWatchList() = viewModelScope.launch {
        inWatchList.value = repository.checkIsInWatchList(movieR.value!!.id ?: 0)
    }

    fun makeMovieForAddToWatchList() {
        movieR.let {
            val movie = WatchListMovie(
                it.value!!.backdrop_path.toString(),
                it.value!!.poster_path.toString(),
                it.value!!.id ?: 0,
                it.value!!.original_language.toString(),
                it.value!!.original_title.toString(),
                it.value!!.title.toString(),
                it.value!!.release_date.toString(),
                it.value!!.overview.toString(),
                it.value!!.popularity.toString(),
                it.value!!.vote_average.toString(),
                it.value!!.vote_count ?: 0
            )
            insertWatchListMovie(movie)
        }

    }

    fun makeMovieForAddToFavorites() {
        movieR.let {
            val movie = FavoriteMovie(
                it.value!!.backdrop_path.toString(),
                it.value!!.poster_path.toString(),
                it.value!!.id ?: 0,
                it.value!!.original_language.toString(),
                it.value!!.original_title.toString(),
                it.value!!.title.toString(),
                it.value!!.release_date.toString(),
                it.value!!.overview.toString(),
                it.value!!.popularity.toString(),
                it.value!!.vote_average.toString(),
                it.value!!.vote_count ?: 0
            )
            insertFavoriteMovie(movie)
        }

    }

    fun getWatchListMovies() = viewModelScope.launch {
        watchListMovies.value = repository.getWatchListMovies().value
    }

    fun getFavoriteMovies() = viewModelScope.launch {
        favoriteMovies.value = repository.getFavoriteMovies().value
    }

    fun setItem(item: MovieResult?) {
        movie.value = item!!
        checkIsInWatchList()
        checkIsInFavorites()

    }

}