package com.cavigna.movieapp.model.models.model.details


import com.google.gson.annotations.SerializedName

data class ProductionCountry(
    @SerializedName("iso_3166_1")
    var iso31661: String = "",
    @SerializedName("name")
    var name: String = ""
)