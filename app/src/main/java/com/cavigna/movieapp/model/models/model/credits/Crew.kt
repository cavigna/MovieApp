package com.cavigna.movieapp.model.models.model.credits


import com.google.gson.annotations.SerializedName

data class Crew(
    @SerializedName("adult")
    var adult: Boolean = false,
    @SerializedName("credit_id")
    var creditId: String = "",
    @SerializedName("department")
    var department: String = "",
    @SerializedName("gender")
    var gender: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("job")
    var job: String = "",
    @SerializedName("known_for_department")
    var knownForDepartment: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("original_name")
    var originalName: String = "",
    @SerializedName("popularity")
    var popularity: Double = 0.0,
    @SerializedName("profile_path")
    var profilePath: String = ""
)