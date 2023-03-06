package com.msy.themoviemanagerpro.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.msy.themoviemanagerpro.R
import com.msy.themoviemanagerpro.database.FavoriteMovie
import com.msy.themoviemanagerpro.repo.FakeMovieRepositoryTest
import com.msy.themoviemanagerpro.ui.FragmentFactory
import com.msy.themoviemanagerpro.ui.adapter.FavoriteMovieAdapter
import com.msy.themoviemanagerpro.ui.fragment.FavoriteScreen
import com.msy.themoviemanagerpro.viewmodel.DetailScreenViewModel
import com.msy.themoviemanagerpro.viewmodel.FavoriteScreenViewModel
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
class FavoriteScreenTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private lateinit var testViewModel: FavoriteScreenViewModel
    private lateinit var detailTestViewModel: DetailScreenViewModel
    private lateinit var favoriteMovieResult: FavoriteMovie

    private lateinit var favoriteMovieList: List<FavoriteMovie>

    @Before
    fun setup() {
        hiltRule.inject()

        favoriteMovieResult = FavoriteMovie(
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


        testViewModel = FavoriteScreenViewModel(FakeMovieRepositoryTest())
        detailTestViewModel = DetailScreenViewModel(FakeMovieRepositoryTest())


    }

    @Test
    fun testFavoritesVisibilityTrue() {
        val navController = Mockito.mock(NavController::class.java)

        favoriteMovieList = listOf()

        launchFragmentInHiltContainer<FavoriteScreen>(factory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            favoriteMovieAdapter.movieList = favoriteMovieList
            viewModel = testViewModel
            detailViewModel = detailTestViewModel
            this.setVisibility(favoriteMovieList)
        }

        onView(ViewMatchers.withId(R.id.favoritesBackground))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))


    }

    @Test
    fun testFavoritesVisibilityFalse() {
        val navController = Mockito.mock(NavController::class.java)

        favoriteMovieList = listOf(favoriteMovieResult)

        launchFragmentInHiltContainer<FavoriteScreen>(factory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            favoriteMovieAdapter.movieList = favoriteMovieList
            viewModel = testViewModel
            detailViewModel = detailTestViewModel
            this.setVisibility(favoriteMovieList)
        }

        onView(ViewMatchers.withId(R.id.favoritesBackground))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))


    }

    @Test
    fun testWatchListToDetailsFragment() {
        val navController = Mockito.mock(NavController::class.java)

        favoriteMovieList = listOf(favoriteMovieResult)

        launchFragmentInHiltContainer<FavoriteScreen>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
            detailViewModel = detailTestViewModel
            this.setVisibility(favoriteMovieList)
            favoriteMovieAdapter.movieList = favoriteMovieList
        }

        onView(ViewMatchers.withId(R.id.moviesListRecyclerFavorite)).perform(
            RecyclerViewActions.actionOnItemAtPosition<FavoriteMovieAdapter.MovieViewHolder>(
                0,
                ViewActions.click()
            )
        )

        Mockito.verify(navController).navigate(R.id.action_favoriteScreen_to_detailScreen)
    }
}
