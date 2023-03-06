package com.msy.themoviemanagerpro.ui.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.msy.themoviemanagerpro.R
import com.msy.themoviemanagerpro.databinding.FragmentDetailScreenBinding
import com.msy.themoviemanagerpro.model.MovieResult
import com.msy.themoviemanagerpro.ui.activity.MainActivity
import com.msy.themoviemanagerpro.viewmodel.DetailScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class DetailScreen @Inject constructor(private var glide: RequestManager) : Fragment() {

    private var _binding: FragmentDetailScreenBinding? = null
    private val fragmentDetailScreenBinding get() = _binding!!

    lateinit var viewModel: DetailScreenViewModel

    var favorite = false
    var watchList = false
    private var id = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailScreenBinding.inflate(inflater, container, false)

        return fragmentDetailScreenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[DetailScreenViewModel::class.java]

        fragmentDetailScreenBinding.detailsToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.appbarAddWatchlist -> {
                    if (watchList) {
                        it.setIcon(R.drawable.ic_round_playlist_add_24)
                        viewModel.deleteWatchListMovie(id)
                        Toast.makeText(
                            requireContext(),
                            R.string.deleted_from_watchlist,
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        it.setIcon(R.drawable.ic_round_playlist_remove_24)
                        viewModel.makeMovieForAddToWatchList()
                        Toast.makeText(
                            requireContext(),
                            R.string.added_to_watchlist,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    true
                }
                R.id.appbarAddFavorite -> {
                    if (favorite) {
                        it.setIcon(R.drawable.ic_baseline_favorite_border_24)
                        viewModel.deleteFavoriteMovie(id)
                        Toast.makeText(
                            requireContext(),
                            R.string.deleted_from_favorites,
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        it.setIcon(R.drawable.ic_round_favorite_24)
                        viewModel.makeMovieForAddToFavorites()
                        Toast.makeText(
                            requireContext(),
                            R.string.added_to_favorites,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    true
                }
                else -> false
            }
        }

        fragmentDetailScreenBinding.detailsToolbarBack.setOnClickListener {
            val previousFragment =
                findNavController().previousBackStackEntry?.destination?.label.toString()
            previousFragment.let {
                when (previousFragment) {
                    "fragment_search_screen" -> {
                        findNavController().navigate(R.id.searchScreen)
                        if (activity is MainActivity) {
                            (activity as MainActivity).navigationView.menu[0].isChecked = true
                        }
                    }

                    "fragment_watch_list_screen" -> {
                        findNavController().navigate(R.id.watchListScreen)
                        if (activity is MainActivity) {
                            (activity as MainActivity).navigationView.menu[1].isChecked = true
                        }
                    }

                    "fragment_favorite_screen" -> {
                        findNavController().navigate(R.id.favoriteScreen)
                        if (activity is MainActivity) {
                            (activity as MainActivity).navigationView.menu[2].isChecked = true
                        }
                    }
                }
            }
        }

        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            var job: Job? = null
            var job2: Job? = null
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    job?.cancel()
                    job = lifecycleScope.launch {
                        delay(50)
                        fragmentDetailScreenBinding.moreAbout.visibility = View.GONE
                    }

                } else {
                    job2?.cancel()
                    job2 = lifecycleScope.launch {
                        delay(50)
                        fragmentDetailScreenBinding.moreAbout.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        }

        val modalBottomSheetBehavior =
            BottomSheetBehavior.from(fragmentDetailScreenBinding.standardBottomSheet)
        modalBottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)


        subscribeToObservers()

    }

    private fun subscribeToObservers() {
        viewModel.movieR.observe(viewLifecycleOwner) {
            setMovieDetails(it)
        }

        viewModel.inFavoriteR.observe(viewLifecycleOwner) {
            favorite = it
            Log.d("semih", it.toString())
            setFavoriteMenuItemDrawable()
        }

        viewModel.inWatchListR.observe(viewLifecycleOwner) {
            watchList = it
            Log.d("semih", it.toString())
            setWatchListMenuItemDrawable()
        }

    }


    fun setFavoriteMenuItemDrawable() {
        val favoriteItem =
            fragmentDetailScreenBinding.detailsToolbar.menu.findItem(R.id.appbarAddFavorite)

        if (favorite) {
            favoriteItem.setIcon(R.drawable.ic_round_favorite_24)
        } else {
            favoriteItem.setIcon(R.drawable.ic_baseline_favorite_border_24)
        }

    }

    fun setWatchListMenuItemDrawable() {
        val watchlistItem =
            fragmentDetailScreenBinding.detailsToolbar.menu.findItem(R.id.appbarAddWatchlist)

        if (watchList) {
            watchlistItem.setIcon(R.drawable.ic_round_playlist_remove_24)
        } else {
            watchlistItem.setIcon(R.drawable.ic_round_playlist_add_24)
        }
    }

    private fun setDate(date: String): String {
        if (!date.isNullOrEmpty()) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val dateF: Date = inputFormat.parse(date)!!
            return outputFormat.format(dateF)
        } else {
            return ""
        }
    }

    fun setMovieDetails(movie: MovieResult) {
        movie.let {
            fragmentDetailScreenBinding.movieDate.text = setDate(it.release_date!!)
            fragmentDetailScreenBinding.movieTitle.text = it.title
            fragmentDetailScreenBinding.movieTitle2.text = it.title
            fragmentDetailScreenBinding.movieOverview.text = it.overview
            fragmentDetailScreenBinding.movieVoteAverage.text = it.vote_average.toString()
            fragmentDetailScreenBinding.moviePopularity.text = it.popularity.toString()
            fragmentDetailScreenBinding.movieDate2.text = setDate(it.release_date!!)
            fragmentDetailScreenBinding.moviePopularity2.text = it.popularity.toString()
            fragmentDetailScreenBinding.voteCount.text = it.vote_count.toString()
            fragmentDetailScreenBinding.originalTitle.text = it.original_title.toString()
            fragmentDetailScreenBinding.originalLanguage.text = it.original_language.toString()
            id = it.id!!

            val url = it.backdrop_path
            val fullPath = "https://image.tmdb.org/t/p/w500$url"
            glide
                .load(fullPath)
                .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    fragmentDetailScreenBinding.detailProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    fragmentDetailScreenBinding.detailProgressBar.visibility = View.INVISIBLE
                    return false
                }

            }).into(fragmentDetailScreenBinding.movieBackDrop)


        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}