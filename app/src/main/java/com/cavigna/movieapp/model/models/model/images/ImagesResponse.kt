package com.cavigna.movieapp.model.models.model.images


import com.google.gson.annotations.SerializedName

data class ImagesResponse(
    @SerializedName("backdrops")
    var backdrops: List<Backdrop> = listOf(),
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("logos")
    var logos: List<Logo> = listOf(),
    @SerializedName("posters")
    var posters: List<Poster> = listOf()
)