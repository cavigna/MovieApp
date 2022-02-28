package com.cavigna.movieapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cavigna.movieapp.databinding.ImageRowBinding
import com.cavigna.movieapp.model.models.model.images.Backdrop
import com.cavigna.movieapp.utils.fillPathTMDB


class BackDropAdapter : ListAdapter<Backdrop, ImageHolder>(ComparadorImage()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        return ImageHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val backdrop = getItem(position)

        holder.binding.imageView.load(fillPathTMDB(backdrop.filePath))
    }
}


class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding: ImageRowBinding = ImageRowBinding.bind(itemView)

    companion object {
        fun create(parent: ViewGroup): ImageHolder {
            val layoutInflaterB = LayoutInflater.from(parent.context)
            val binding = ImageRowBinding.inflate(layoutInflaterB, parent, false)

            return ImageHolder(binding.root)
        }
    }


}

class ComparadorImage : DiffUtil.ItemCallback<Backdrop>() {
    override fun areItemsTheSame(oldItem: Backdrop, newItem: Backdrop): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Backdrop, newItem: Backdrop): Boolean {
        return oldItem.filePath == newItem.filePath
    }

}