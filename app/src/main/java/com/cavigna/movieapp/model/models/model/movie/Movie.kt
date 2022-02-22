package com.cavigna.movieapp.model.models.model.movie


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies_popular_table")
data class Movie(
    @PrimaryKey
    var id: Int = 0,
    var title: String = "",
    var overview: String = "",

    @SerializedName("release_date")
    var releaseDate: String = "",

    var popularity: Double = 0.0,

    @SerializedName("vote_average")
    var voteAverage: Double = 0.0,

    @SerializedName("vote_count")
    var voteCount: Int = 0,

    @SerializedName("original_language")
    var originalLanguage: String = "",

    var adult: Boolean = false,

    @SerializedName("genre_ids")
    var genreIds: List<Int> = listOf(),

    @SerializedName("original_title")
    var originalTitle: String = "",

    @SerializedName("poster_path")
    var posterPath: String = "",

    @SerializedName("backdrop_path")
    //@ColumnInfo(defaultValue = "")
    var backdropPath: String = "",


    var video: Boolean = false,

    @ColumnInfo(defaultValue = "0")
    var searched: Boolean =false

)