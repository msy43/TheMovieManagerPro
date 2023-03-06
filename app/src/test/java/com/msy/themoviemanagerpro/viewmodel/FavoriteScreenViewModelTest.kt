package com.msy.themoviemanagerpro.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.msy.themoviemanagerpro.MainCoroutineRule
import com.msy.themoviemanagerpro.database.FavoriteMovie
import com.msy.themoviemanagerpro.repo.FakeMovieRepository
import getOrAwaitValueTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoriteScreenViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: FavoriteScreenViewModel
    private lateinit var favoriteMovie: FavoriteMovie

    @Before
    fun setup() {
        favoriteMovie = FavoriteMovie(
            "a",
            "a",
            1,
            "a",
            "a",
            "a",
            "2023-01-01",
            "a",
            "1",
            "1",
            1
        )

        viewModel = FavoriteScreenViewModel(FakeMovieRepository())
    }


    @Test
    fun `get favorite movie list and check without return error`() {

        val job = viewModel.getFavoriteMovies()

        Truth.assertThat(job.isCompleted).isTrue()

    }

    @Test
    fun `set favorite movie list and check without return error`() {

        viewModel.setFavoriteMovies(favoriteMovie)

        viewModel.getFavoriteMovies()

        Truth.assertThat(viewModel.favoriteMovies.getOrAwaitValueTest()[0].api_id)
            .isEqualTo(favoriteMovie.api_id)
    }

}