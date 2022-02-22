package com.cavigna.movieapp.model.models.model.credits


import com.google.gson.annotations.SerializedName


data class CreditResponse(
    @SerializedName("cast")
    var cast: List<Cast> = listOf(),
    @SerializedName("crew")
    var crew: List<Crew> = listOf(),
    @SerializedName("id")
    var id: Int = 0
)