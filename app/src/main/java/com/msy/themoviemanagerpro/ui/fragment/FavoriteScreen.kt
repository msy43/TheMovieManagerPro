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
import com.msy.themoviemanagerpro.database.FavoriteMovie
import com.msy.themoviemanagerpro.databinding.FragmentFavoriteScreenBinding
import com.msy.themoviemanagerpro.model.MovieResult
import com.msy.themoviemanagerpro.ui.adapter.FavoriteMovieAdapter
import com.msy.themoviemanagerpro.viewmodel.DetailScreenViewModel
import com.msy.themoviemanagerpro.viewmodel.FavoriteScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteScreen @Inject constructor(var favoriteMovieAdapter: FavoriteMovieAdapter) :
    Fragment() {

    private var _binding: FragmentFavoriteScreenBinding? = null
    private val fragmentFavoriteScreenBinding get() = _binding!!

    lateinit var viewModel: FavoriteScreenViewModel
    lateinit var detailViewModel: DetailScreenViewModel

    lateinit var moviesRecyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteScreenBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[FavoriteScreenViewModel::class.java]
        detailViewModel = ViewModelProvider(requireActivity())[DetailScreenViewModel::class.java]

        moviesRecyclerView = fragmentFavoriteScreenBinding.moviesListRecyclerFavorite

        return fragmentFavoriteScreenBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviesRecyclerView.adapter = favoriteMovieAdapter
        moviesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        subscribeToObservers()

        favoriteMovieAdapter.setOnItemClickListener {
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
            findNavController().navigate(R.id.action_favoriteScreen_to_detailScreen)
        }

    }

    private fun subscribeToObservers() {
        viewModel.favoriteMovies.observe(viewLifecycleOwner) {
            favoriteMovieAdapter.movieList = it
            setVisibility(it)
        }
    }

    fun setVisibility(list: List<FavoriteMovie>) {
        if (list.isNotEmpty()) {
            fragmentFavoriteScreenBinding.favoritesBackground.visibility = View.GONE
        } else {
            fragmentFavoriteScreenBinding.favoritesBackground.visibility = View.VISIBLE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}