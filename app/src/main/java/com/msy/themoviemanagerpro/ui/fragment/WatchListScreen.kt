package com.msy.themoviemanagerpro.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.msy.themoviemanagerpro.R
import com.msy.themoviemanagerpro.database.WatchListMovie
import com.msy.themoviemanagerpro.databinding.FragmentWatchListScreenBinding
import com.msy.themoviemanagerpro.model.MovieResult
import com.msy.themoviemanagerpro.ui.adapter.WatchListMovieAdapter
import com.msy.themoviemanagerpro.viewmodel.DetailScreenViewModel
import com.msy.themoviemanagerpro.viewmodel.WatchListScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WatchListScreen @Inject constructor(var watchListMovieAdapter: WatchListMovieAdapter) :
    Fragment() {

    private var _binding: FragmentWatchListScreenBinding? = null
    private val fragmentWatchListScreenBinding get() = _binding!!

    lateinit var viewModel: WatchListScreenViewModel
    lateinit var detailViewModel: DetailScreenViewModel

    lateinit var moviesRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWatchListScreenBinding.inflate(layoutInflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[WatchListScreenViewModel::class.java]
        detailViewModel = ViewModelProvider(requireActivity())[DetailScreenViewModel::class.java]

        moviesRecyclerView = fragmentWatchListScreenBinding.moviesListRecyclerWatchlist


        return fragmentWatchListScreenBinding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviesRecyclerView.adapter = watchListMovieAdapter
        moviesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        subscribeToObservers()

        watchListMovieAdapter.setOnItemClickListener {
            val selectedMovie =
                MovieResult(
                    it.backdrop_path,
                    it.poster_path,
                    it.api_id,
                    it.original_language,
                    it.original_title,
                    it.title,
                    it.release_date,
                    it.overview,
                    it.popularity.toFloat(),
                    it.vote_average.toFloat(),
                    it.vote_count
                )
            detailViewModel.setItem(selectedMovie)
            findNavController().navigate(R.id.action_watchListScreen_to_detailScreen)
        }

    }

    private fun subscribeToObservers() {
        viewModel.watchListMovies.observe(viewLifecycleOwner) {
            watchListMovieAdapter.movieList = it
            setVisibility(it)
        }
    }

    fun setVisibility(list: List<WatchListMovie>) {
        if (list.isNotEmpty()) {
            fragmentWatchListScreenBinding.watchListBackground.visibility = View.GONE
        } else {
            fragmentWatchListScreenBinding.watchListBackground.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}