package com.cavigna.movieapp.model.models.model.movie


import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("page")
    var page: Int = 0,
    @SerializedName("results")
    var popularMovies: List<Movie> = listOf(),
    @SerializedName("total_pages")
    var totalPages: Int = 0,
    @SerializedName("total_results")
    var totalResults: Int = 0
)