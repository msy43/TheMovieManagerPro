package com.msy.themoviemanagerpro.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.msy.themoviemanagerpro.ui.adapter.FavoriteMovieAdapter
import com.msy.themoviemanagerpro.ui.adapter.SearchMovieAdapter
import com.msy.themoviemanagerpro.ui.adapter.WatchListMovieAdapter
import com.msy.themoviemanagerpro.ui.fragment.DetailScreen
import com.msy.themoviemanagerpro.ui.fragment.FavoriteScreen
import com.msy.themoviemanagerpro.ui.fragment.SearchScreen
import com.msy.themoviemanagerpro.ui.fragment.WatchListScreen
import javax.inject.Inject

class FragmentFactory @Inject constructor(
    private val glide: RequestManager,
    private val searchMovieAdapter: SearchMovieAdapter,
    private val watchListMovieAdapter: WatchListMovieAdapter,
    private val favoriteMovieAdapter: FavoriteMovieAdapter
) :
    FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when (className) {
            SearchScreen::class.java.name -> SearchScreen(searchMovieAdapter)
            DetailScreen::class.java.name -> DetailScreen(glide)
            WatchListScreen::class.java.name -> WatchListScreen(watchListMovieAdapter)
            FavoriteScreen::class.java.name -> FavoriteScreen(favoriteMovieAdapter)
            else -> return super.instantiate(classLoader, className)
        }


    }

}