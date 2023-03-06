package com.msy.themoviemanagerpro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msy.themoviemanagerpro.database.FavoriteMovie
import com.msy.themoviemanagerpro.repo.MovieRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteScreenViewModel @Inject constructor(var repository: MovieRepositoryInterface) :
    ViewModel() {

    val favoriteMovies = repository.getFavoriteMovies()

    fun getFavoriteMovies() = viewModelScope.launch {
        repository.getFavoriteMovies()
    }

    fun setFavoriteMovies(movie: FavoriteMovie) {
        viewModelScope.launch {
            repository.insertFavoriteMovie(movie)
        }
    }

}