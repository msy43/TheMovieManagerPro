package com.msy.themoviemanagerpro.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.msy.themoviemanagerpro.R
import com.msy.themoviemanagerpro.databinding.FragmentSearchScreenBinding
import com.msy.themoviemanagerpro.ui.adapter.SearchMovieAdapter
import com.msy.themoviemanagerpro.util.Status
import com.msy.themoviemanagerpro.viewmodel.DetailScreenViewModel
import com.msy.themoviemanagerpro.viewmodel.SearchScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchScreen @Inject constructor(private var moviesAdapter: SearchMovieAdapter) : Fragment() {

    private var _binding: FragmentSearchScreenBinding? = null
    private val fragmentSearchScreenBinding get() = _binding!!

    private lateinit var viewModel: SearchScreenViewModel
    private lateinit var detailViewModel: DetailScreenViewModel

    private lateinit var moviesRecyclerView: RecyclerView
    private lateinit var searchInput: android.widget.SearchView
    private lateinit var progressBar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchScreenBinding.inflate(layoutInflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[SearchScreenViewModel::class.java]
        detailViewModel = ViewModelProvider(requireActivity())[DetailScreenViewModel::class.java]


        moviesRecyclerView = fragmentSearchScreenBinding.moviesListRecycler
        searchInput = fragmentSearchScreenBinding.searchButton
        progressBar = fragmentSearchScreenBinding.moviesProgressBar

        return fragmentSearchScreenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers()

        searchInput.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                p0.let {
                    if (it!!.isNotEmpty()) {
                        viewModel.searchMovie(it)
                    }
                }

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        moviesRecyclerView.adapter = moviesAdapter
        moviesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())



        moviesAdapter.setOnItemClickListener {
            detailViewModel.setItem(it)
            findNavController().navigate(R.id.detailScreen)
        }
    }

    private fun subscribeToObservers() {
        viewModel.movieList.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    val movies = it.data?.results

                    if (movies != null) {
                        if (movies.isNotEmpty()) {
                            fragmentSearchScreenBinding.searchBackground.visibility = View.INVISIBLE
                            moviesAdapter.movieList = movies
                            val llm: LinearLayoutManager =
                                moviesRecyclerView.layoutManager as LinearLayoutManager
                            llm.stackFromEnd = true
                            llm.scrollToPositionWithOffset(0, 0)
                        } else {
                            movies.clear()
                            moviesAdapter.movieList = movies
                            fragmentSearchScreenBinding.searchBackground.visibility = View.VISIBLE
                        }

                    }

                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    fragmentSearchScreenBinding.searchBackground.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}