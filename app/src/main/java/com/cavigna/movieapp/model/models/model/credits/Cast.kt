package com.cavigna.movieapp.model.models.model.credits


import com.google.gson.annotations.SerializedName

data class Cast(
    @SerializedName("adult")
    var adult: Boolean = false,
    @SerializedName("cast_id")
    var castId: Int = 0,
    @SerializedName("character")
    var character: String = "",
    @SerializedName("credit_id")
    var creditId: String = "",
    @SerializedName("gender")
    var gender: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("known_for_department")
    var knownForDepartment: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("order")
    var order: Int = 0,
    @SerializedName("original_name")
    var originalName: String = "",
    @SerializedName("popularity")
    var popularity: Double = 0.0,
    @SerializedName("profile_path")
    var profilePath: String = ""
)