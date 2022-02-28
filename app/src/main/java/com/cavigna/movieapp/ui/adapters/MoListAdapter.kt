package com.cavigna.movieapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cavigna.movieapp.R
import com.cavigna.movieapp.databinding.ItemRowVerticalBinding
import com.cavigna.movieapp.model.models.model.movie.Movie

class MoListAdapter(
    private val extraerId: MoPageAdapter.ExtraerId,
    private val origen: String ="home"
    )
    : ListAdapter<Movie, MyViewHolder>(MyViewHolder.Comparador()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = getItem(position)
        with(holder.binding) {
            tvTituloRow.text = movie.title
            tvVoteAvergae.text = movie.voteAverage.toString()
            imageView.load("https://image.tmdb.org/t/p/original${movie.posterPath}")
            ratingBar.rating = movie.voteAverage.toFloat()

            holder.binding. card.setOnClickListener {
                extraerId.alHacerClick(movie.id)
                when (origen) {
                    "home" -> Navigation.findNavController(holder.itemView).navigate(R.id.action_homeFragment_to_detailsFragment)
                    "search" -> Navigation.findNavController(holder.itemView).navigate(R.id.action_searchFragment_to_detailsFragment)
                    "upcoming" -> Navigation.findNavController(holder.itemView).navigate(R.id.action_upcomingFragment_to_detailsFragment)
                    "favorite" -> Navigation.findNavController(holder.itemView).navigate(R.id.action_favoriteFragment_to_detailsFragment)
                }



            }
        }
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val binding: ItemRowVerticalBinding = ItemRowVerticalBinding.bind(itemView)

    companion object {
        fun create(parent: ViewGroup): MyViewHolder {
            val layoutInflaterB = LayoutInflater.from(parent.context)
            val binding = ItemRowVerticalBinding.inflate(layoutInflaterB, parent, false)

            return MyViewHolder(binding.root)
        }
    }

    class Comparador : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }


    }
}
