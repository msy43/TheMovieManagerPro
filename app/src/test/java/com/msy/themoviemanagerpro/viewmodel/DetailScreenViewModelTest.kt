package com.msy.themoviemanagerpro.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.msy.themoviemanagerpro.MainCoroutineRule
import com.msy.themoviemanagerpro.model.MovieResult
import com.msy.themoviemanagerpro.repo.FakeMovieRepository
import getOrAwaitValueTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailScreenViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: DetailScreenViewModel
    private lateinit var movieResult: MovieResult

    @Before
    fun setup() {
        movieResult = MovieResult(
            "a",
            "a",
            1,
            "a",
            "a",
            "a",
            "2023-01-01",
            "a",
            1,
            1,
            1
        )

        viewModel = DetailScreenViewModel(FakeMovieRepository())
    }

    @Test
    fun `insert favorite movie and check without return error`() {

        viewModel.setItem(movieResult)
        viewModel.makeMovieForAddToFavorites()
        viewModel.getFavoriteMovies()

        assertThat(viewModel.favoriteMoviesR.getOrAwaitValueTest()[0].api_id).isEqualTo(movieResult.id)
        assertThat(viewModel.inFavoriteR.getOrAwaitValueTest()).isTrue()
    }

    @Test
    fun `delete favorite movie without return error`() {

        viewModel.setItem(movieResult)
        viewModel.makeMovieForAddToFavorites()
        val movieR = viewModel.movieR.getOrAwaitValueTest()
        viewModel.deleteFavoriteMovie(movieR.id ?: 0)
        viewModel.getFavoriteMovies()

        assertThat(viewModel.favoriteMoviesR.getOrAwaitValueTest()).isEmpty()
        assertThat(viewModel.inFavoriteR.getOrAwaitValueTest()).isFalse()
    }

    @Test
    fun `insert watchlist movie check without return error`() {

        viewModel.setItem(movieResult)
        viewModel.makeMovieForAddToWatchList()
        viewModel.getWatchListMovies()

        assertThat(viewModel.watchListMoviesR.getOrAwaitValueTest()[0].api_id).isEqualTo(movieResult.id)
        assertThat(viewModel.inWatchListR.getOrAwaitValueTest()).isTrue()
    }

    @Test
    fun `delete watchlist movie without return error`() {

        viewModel.setItem(movieResult)
        viewModel.makeMovieForAddToWatchList()
        val movieR = viewModel.movieR.getOrAwaitValueTest()
        viewModel.deleteWatchListMovie(movieR.id ?: 0)
        viewModel.getWatchListMovies()

        assertThat(viewModel.watchListMoviesR.getOrAwaitValueTest()).isEmpty()
        assertThat(viewModel.inWatchListR.getOrAwaitValueTest()).isFalse()
    }

}