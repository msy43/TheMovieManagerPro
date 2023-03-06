package com.msy.themoviemanagerpro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msy.themoviemanagerpro.model.MovieResponse
import com.msy.themoviemanagerpro.repo.MovieRepositoryInterface
import com.msy.themoviemanagerpro.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(var repository: MovieRepositoryInterface) : ViewModel() {


    private val movies = MutableLiveData<Resource<MovieResponse>>()
    val movieList: LiveData<Resource<MovieResponse>> get() = movies

    fun searchMovie(movie: String) {
        if (movie.isEmpty()) {
            return
        } else {
            movies.value = Resource.loading(null)
            viewModelScope.launch {
                val response = repository.searchMovie(movie)
                movies.value = response
            }
        }
    }


}