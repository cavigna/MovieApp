package com.cavigna.movieapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cavigna.movieapp.R
import com.cavigna.movieapp.databinding.ItemRowVerticalBinding
import com.cavigna.movieapp.model.models.model.movie.Movie

class MoPageAdapter(val extraerId: ExtraerId) : PagingDataAdapter<Movie, MovieViewHolder>(ComparadorPage()) {
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie !=null){
            with(holder.binding) {
                tvTituloRow.text = movie.title
                tvVoteAvergae.text = movie.voteAverage.toString()
                imageView.load("https://image.tmdb.org/t/p/original${movie.posterPath}")
                //ratingBar.numStars = 10
                //ratingBar.numStars = movie.voteAverage.toFloat().toInt()
                //ratingBar.rating = movie.voteAverage.toFloat()
                ratingBar.rating = ((movie.voteAverage*5/10).toFloat())

                holder.binding. card.setOnClickListener {
                    extraerId.alHacerClick(movie.id)
                    Navigation.findNavController(holder.itemView).navigate(R.id.action_homeFragment_to_detailsFragment)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.create(parent)
    }

    interface ExtraerId{
        fun alHacerClick(id:Int)
    }

}



class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
   // val binding: ItemRowBinding = ItemRowBinding.bind(itemView)
   val binding: ItemRowVerticalBinding = ItemRowVerticalBinding.bind(itemView)

    companion object {
        fun create(parent: ViewGroup): MovieViewHolder {
            val layoutInflaterB = LayoutInflater.from(parent.context)
            val binding = ItemRowVerticalBinding.inflate(layoutInflaterB, parent, false)
            //val binding = ItemRowBinding.inflate(layoutInflaterB, parent, false)

            return MovieViewHolder(binding.root)
        }

    }


}

class ComparadorPage : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }
}
