package com.msy.themoviemanagerpro.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.msy.themoviemanagerpro.MainCoroutineRule
import com.msy.themoviemanagerpro.database.WatchListMovie
import com.msy.themoviemanagerpro.repo.FakeMovieRepository
import getOrAwaitValueTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WatchListScreenViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: WatchListScreenViewModel
    private lateinit var watchListMovie: WatchListMovie

    @Before
    fun setup() {
        watchListMovie = WatchListMovie(
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

        viewModel = WatchListScreenViewModel(FakeMovieRepository())
    }

    @Test
    fun `get watchList movie list and check without return error`() {

        val job = viewModel.getWatchListMovies()

        Truth.assertThat(job.isCompleted).isTrue()

    }

    @Test
    fun `set watchList movie list and check without return error`() {

        viewModel.setWatchListMovie(watchListMovie)

        viewModel.getWatchListMovies()

        Truth.assertThat(viewModel.watchListMovies.getOrAwaitValueTest()[0].api_id)
            .isEqualTo(watchListMovie.api_id)
    }

}