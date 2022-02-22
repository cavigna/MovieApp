package com.cavigna.movieapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cavigna.movieapp.databinding.ItemRowVerticalBinding
import com.cavigna.movieapp.model.models.model.movie.MovieDetail

class DetailsListAdapter : ListAdapter<MovieDetail, DetailViewHolder>(ComparadorDetail()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        return DetailViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val movie = getItem(position)
        with(holder.binding) {
            tvTituloRow.text = movie.title
            tvVoteAvergae.text = movie.voteAverage.toString()
            imageView.load("https://image.tmdb.org/t/p/original${movie.posterPath}")
            //ratingBar.numStars = movie.voteAverage.toFloat().toInt()
            ratingBar.rating = movie.voteAverage.toFloat()
        }
    }

}

class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemRowVerticalBinding.bind(itemView)

    companion object {
        fun create(parent: ViewGroup): DetailViewHolder {
            val binding = ItemRowVerticalBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return DetailViewHolder(binding.root)
        }
    }

}

class ComparadorDetail : ItemCallback<MovieDetail>() {
    override fun areItemsTheSame(oldItem: MovieDetail, newItem: MovieDetail): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MovieDetail, newItem: MovieDetail): Boolean {
        return oldItem.id == newItem.id
    }

}