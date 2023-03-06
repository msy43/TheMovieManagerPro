package com.msy.themoviemanagerpro.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.msy.themoviemanagerpro.R
import com.msy.themoviemanagerpro.database.WatchListMovie
import com.msy.themoviemanagerpro.repo.FakeMovieRepositoryTest
import com.msy.themoviemanagerpro.ui.FragmentFactory
import com.msy.themoviemanagerpro.ui.adapter.WatchListMovieAdapter
import com.msy.themoviemanagerpro.ui.fragment.WatchListScreen
import com.msy.themoviemanagerpro.viewmodel.DetailScreenViewModel
import com.msy.themoviemanagerpro.viewmodel.WatchListScreenViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
class WatchListScreenTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private lateinit var testViewModel: WatchListScreenViewModel
    private lateinit var detailTestViewModel: DetailScreenViewModel
    private lateinit var watchListMovieResult: WatchListMovie

    private lateinit var watchListMovieList: List<WatchListMovie>

    @Before
    fun setup() {
        hiltRule.inject()

        watchListMovieResult = WatchListMovie(
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
            1,
            1
        )


        detailTestViewModel = DetailScreenViewModel(FakeMovieRepositoryTest())
        testViewModel = WatchListScreenViewModel(FakeMovieRepositoryTest())


    }

    @Test
    fun testWatchListVisibilityTrue() {
        val navController = Mockito.mock(NavController::class.java)

        watchListMovieList = listOf()

        launchFragmentInHiltContainer<WatchListScreen>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
            watchListMovieAdapter.movieList = watchListMovieList
            viewModel = testViewModel
            detailViewModel = detailTestViewModel
            this.setVisibility(watchListMovieList)
        }

        onView(withId(R.id.watchListBackground)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun testWatchListVisibilityFalse() {
        val navController = Mockito.mock(NavController::class.java)

        watchListMovieList = listOf(watchListMovieResult)

        launchFragmentInHiltContainer<WatchListScreen>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
            watchListMovieAdapter.movieList = watchListMovieList
            viewModel = testViewModel
            detailViewModel = detailTestViewModel
            this.setVisibility(watchListMovieList)
        }

        onView(withId(R.id.watchListBackground)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun testWatchListToDetailsFragment() {
        val navController = Mockito.mock(NavController::class.java)

        watchListMovieList = listOf(watchListMovieResult)

        launchFragmentInHiltContainer<WatchListScreen>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
            watchListMovieAdapter.movieList = watchListMovieList
            viewModel = testViewModel
            detailViewModel = detailTestViewModel
            this.setVisibility(watchListMovieList)
        }

        onView(withId(R.id.moviesListRecyclerWatchlist)).perform(
            RecyclerViewActions.actionOnItemAtPosition<WatchListMovieAdapter.MovieViewHolder>(
                0,
                ViewActions.click()
            )
        )

        Mockito.verify(navController).navigate(R.id.action_watchListScreen_to_detailScreen)
    }

}