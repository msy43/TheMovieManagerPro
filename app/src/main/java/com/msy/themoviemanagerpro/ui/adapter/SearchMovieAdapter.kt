package com.msy.themoviemanagerpro.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.msy.themoviemanagerpro.R
import com.msy.themoviemanagerpro.model.MovieResult
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SearchMovieAdapter @Inject constructor(var glide: RequestManager) :
    RecyclerView.Adapter<SearchMovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var onItemClickListener: ((MovieResult) -> Unit)? = null


    private var diffUtil =

        object : DiffUtil.ItemCallback<MovieResult>() {
            override fun areItemsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: MovieResult,
                newItem: MovieResult
            ): Boolean {
                return oldItem == newItem
            }

        }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var movieList: List<MovieResult>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item_desing, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    fun setOnItemClickListener(listener: (MovieResult) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.moviePoster)
        val title = holder.itemView.findViewById<TextView>(R.id.movieTitle)
        val date = holder.itemView.findViewById<TextView>(R.id.movieDate)
        val rate = holder.itemView.findViewById<TextView>(R.id.movieRate)
        val progressBar = holder.itemView.findViewById<ProgressBar>(R.id.movieItemProgressBar)

        val url = movieList[position].poster_path
        val fullPath = "https://image.tmdb.org/t/p/w500$url"

        holder.itemView.apply {
            title.text = movieList[position].title
            date.text = setDate(movieList[position].release_date.toString())
            rate.text = movieList[position].vote_average.toString()
            glide
                .load(fullPath)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.INVISIBLE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.INVISIBLE
                        return false
                    }

                })
                .into(imageView)
            setOnClickListener {
                onItemClickListener?.let {
                    it(movieList[position])
                }
            }
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
}