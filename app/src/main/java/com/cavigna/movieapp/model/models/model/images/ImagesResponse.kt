package com.cavigna.movieapp.model.models.model.images


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "images_details_table")
data class ImagesResponse(
    @PrimaryKey
    var id: Int = 0,
    var backdrops: List<Backdrop?> = listOf(),
    var logos: List<Logo?> = listOf(),
    var posters: List<Poster?> = listOf()
)