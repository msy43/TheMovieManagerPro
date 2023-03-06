package com.msy.themoviemanagerpro.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class MovieDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    @Named("testDatabase")
    lateinit var database: MovieDatabase

    private lateinit var movieDao: MovieDao

    @Before
    fun setup() {
        hiltRule.inject()

        movieDao = database.movieDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertWatchListMovieTesting() = runTest {
        val exampleWatchListMovie = WatchListMovie(
            "a",
            "a",
            1,
            "a",
            "a",
            "a",
            "2023-01-01",
            "a",
            "a",
            "a",
            1,
            1
        )
        movieDao.insertWatchListMovie(exampleWatchListMovie)

        val list = movieDao.observeWatchListMovies().getOrAwaitValue()

        assertThat(list).contains(exampleWatchListMovie)

    }

    @Test
    fun insertFavoriteMovieTesting() = runTest {
        val exampleFavoriteMovie = FavoriteMovie(
            "a",
            "a",
            1,
            "a",
            "a",
            "a",
            "2023-01-01",
            "a",
            "a",
            "a",
            1,
            1
        )
        movieDao.insertFavoriteMovie(exampleFavoriteMovie)

        val list = movieDao.observeFavoriteMovies().getOrAwaitValue()

        assertThat(list).contains(exampleFavoriteMovie)
    }

    @Test
    fun deleteWatchListMovieTesting() = runTest {
        val exampleWatchListMovie = WatchListMovie(
            "a",
            "a",
            1,
            "a",
            "a",
            "a",
            "2023-01-01",
            "a",
            "a",
            "a",
            1,
            1
        )
        movieDao.insertWatchListMovie(exampleWatchListMovie)
        movieDao.deleteWatchListMovie(exampleWatchListMovie.api_id)

        val list = movieDao.observeWatchListMovies().getOrAwaitValue()

        assertThat(list).doesNotContain(exampleWatchListMovie)

    }

    @Test
    fun deleteFavoriteMovieTesting() = runTest {
        val exampleFavoriteMovie = FavoriteMovie(
            "a",
            "a",
            1,
            "a",
            "a",
            "a",
            "2023-01-01",
            "a",
            "a",
            "a",
            1,
            1
        )
        movieDao.insertFavoriteMovie(exampleFavoriteMovie)
        movieDao.deleteFavoriteMovie(
            exampleFavoriteMovie.api_id
        )
        val list = movieDao.observeFavoriteMovies().getOrAwaitValue()

        assertThat(list).doesNotContain(exampleFavoriteMovie)
    }

    @Test
    fun observeWatchListMovieTesting() = runTest {
        val exampleWatchListMovie = WatchListMovie(
            "a",
            "a",
            1,
            "a",
            "a",
            "a",
            "2023-01-01",
            "a",
            "a",
            "a",
            1,
            1
        )
        movieDao.insertWatchListMovie(exampleWatchListMovie)

        val list = movieDao.observeWatchListMovies().getOrAwaitValue()

        assertThat(list).isNotEmpty()
    }

    @Test
    fun observeFavoriteMovieTesting() = runTest {
        val exampleFavoriteMovie = FavoriteMovie(
            "a",
            "a",
            1,
            "a",
            "a",
            "a",
            "2023-01-01",
            "a",
            "a",
            "a",
            1,
            1
        )
        movieDao.insertFavoriteMovie(exampleFavoriteMovie)

        val list = movieDao.observeFavoriteMovies().getOrAwaitValue()

        assertThat(list).isNotEmpty()
    }

    @Test
    fun checkIsInWatchListTesting() = runTest {
        val exampleWatchListMovie = WatchListMovie(
            "a",
            "a",
            1,
            "a",
            "a",
            "a",
            "2023-01-01",
            "a",
            "a",
            "a",
            1,
            1
        )
        movieDao.insertWatchListMovie(exampleWatchListMovie)
        val check = movieDao.checkIsInWatchList(exampleWatchListMovie.api_id)

        assertThat(check).isTrue()
    }

    @Test
    fun checkIsInFavoritesTesting() = runTest {
        val exampleFavoriteMovie = FavoriteMovie(
            "a",
            "a",
            1,
            "a",
            "a",
            "a",
            "2023-01-01",
            "a",
            "a",
            "a",
            1,
            1
        )
        movieDao.insertFavoriteMovie(exampleFavoriteMovie)
        val check = movieDao.checkIsInFavorites(exampleFavoriteMovie.api_id)

        assertThat(check).isTrue()
    }
}