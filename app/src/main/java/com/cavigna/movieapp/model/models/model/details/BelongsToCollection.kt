package com.cavigna.movieapp.model.models.model.details


import com.google.gson.annotations.SerializedName

data class BelongsToCollection(
    @SerializedName("id")
    var idCollection: Int = 0,
    @SerializedName("backdrop_path")
    var backdropPathCollection: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("poster_path")
    var posterPathCollection: String = ""
)