package com.msy.themoviemanagerpro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msy.themoviemanagerpro.database.WatchListMovie
import com.msy.themoviemanagerpro.repo.MovieRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchListScreenViewModel @Inject constructor(var repository: MovieRepositoryInterface) :
    ViewModel() {

    var watchListMovies = repository.getWatchListMovies()

    fun getWatchListMovies() = viewModelScope.launch {
        repository.getWatchListMovies()
    }

    fun setWatchListMovie(movie: WatchListMovie) {
        viewModelScope.launch {
            repository.insertWatchListMovie(movie)
        }
    }
}