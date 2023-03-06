package com.msy.themoviemanagerpro.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.msy.themoviemanagerpro.R
import com.msy.themoviemanagerpro.model.MovieResult
import com.msy.themoviemanagerpro.repo.FakeMovieRepositoryTest
import com.msy.themoviemanagerpro.ui.FragmentFactory
import com.msy.themoviemanagerpro.ui.fragment.DetailScreen
import com.msy.themoviemanagerpro.viewmodel.DetailScreenViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import launchFragmentInHiltContainer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class DetailScreenTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private lateinit var testViewModel: DetailScreenViewModel
    private lateinit var movieResult: MovieResult


    @Before
    fun setup() {
        hiltRule.inject()

        movieResult = MovieResult(
            "tKa1gmGKAUVYnflYcadipeL3d9h.jpg",
            "qhPtAc1TKbMPqNvcdXSOn9Bn7hZ.jpg",
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

        testViewModel = DetailScreenViewModel(FakeMovieRepositoryTest())

        testViewModel.setItem(movieResult)

    }


    @Test
    fun pressAddFavoritesButtonAndCheckAdded() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<DetailScreen>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
            this.setMovieDetails(movie = movieResult)
        }
        Espresso.onView(ViewMatchers.withId(R.id.appbarAddFavorite)).perform(ViewActions.click())
        testViewModel.getFavoriteMovies()

        assertThat(testViewModel.favoriteMoviesR.getOrAwaitValue()[0].api_id).isEqualTo(1)
        assertThat(testViewModel.inFavoriteR.getOrAwaitValue()).isTrue()
    }

    @Test
    fun pressAddWatchListButtonAndCheckAdded() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<DetailScreen>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
            this.setMovieDetails(movie = movieResult)
        }
        Espresso.onView(ViewMatchers.withId(R.id.appbarAddWatchlist)).perform(ViewActions.click())
        testViewModel.getWatchListMovies()

        assertThat(testViewModel.watchListMoviesR.getOrAwaitValue()[0].api_id).isEqualTo(1)
        assertThat(testViewModel.inWatchListR.getOrAwaitValue()).isTrue()
    }

    @Test
    fun pressAddFavoritesButtonAndCheckDeleted() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<DetailScreen>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
            this.setMovieDetails(movieResult)
            this.favorite = true
            this.setFavoriteMenuItemDrawable()
        }

        Espresso.onView(ViewMatchers.withId(R.id.appbarAddFavorite)).perform(ViewActions.click())
        testViewModel.getFavoriteMovies()

        assertThat(testViewModel.favoriteMoviesR.getOrAwaitValue()).isEmpty()
        assertThat(testViewModel.inFavoriteR.getOrAwaitValue()).isFalse()

    }

    @Test
    fun pressAddWatchListButtonAndCheckDeleted() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<DetailScreen>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
            this.setMovieDetails(movieResult)
            this.watchList = true
            this.setWatchListMenuItemDrawable()
        }

        Espresso.onView(ViewMatchers.withId(R.id.appbarAddWatchlist)).perform(ViewActions.click())
        testViewModel.getWatchListMovies()

        assertThat(testViewModel.watchListMoviesR.getOrAwaitValue()).isEmpty()
        assertThat(testViewModel.inWatchListR.getOrAwaitValue()).isFalse()

    }

}