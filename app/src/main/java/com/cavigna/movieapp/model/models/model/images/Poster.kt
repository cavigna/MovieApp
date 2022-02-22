package com.cavigna.movieapp.model.models.model.images


import com.google.gson.annotations.SerializedName

data class Poster(
    @SerializedName("aspect_ratio")
    var aspectRatio: Double = 0.0,
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("iso_639_1")
    var iso6391: String = "",
    @SerializedName("file_path")
    var filePath: String = "",
    @SerializedName("vote_average")
    var voteAverage: Double = 0.0,
    @SerializedName("vote_count")
    var voteCount: Int = 0,
    @SerializedName("width")
    var width: Int = 0
)