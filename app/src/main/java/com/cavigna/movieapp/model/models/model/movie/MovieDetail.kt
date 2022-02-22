package com.cavigna.movieapp.model.models.model.movie


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.cavigna.movieapp.model.models.model.details.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie_details_table")
data class MovieDetail(

    @SerializedName("id")
    @PrimaryKey
    var id: Int = 0,
    @SerializedName("adult")
    var adult: Boolean = false,

    @ColumnInfo(defaultValue = "0")
    var favorite: Boolean = false,
    @SerializedName("backdrop_path")
    var backdropPath: String = "",

    @Ignore
    @SerializedName("belongs_to_collection")
    var belongsToCollection: BelongsToCollection = BelongsToCollection(),
    @SerializedName("budget")
    var budget: Int = 0,

    @SerializedName("genres")
    var genres: List<Genre> = listOf(),

    @SerializedName("homepage")
    var homepage: String = "",
    @SerializedName("imdb_id")
    var imdbId: String = "",
    @SerializedName("original_language")
    var originalLanguage: String = "",
    @SerializedName("original_title")
    var originalTitle: String = "",
    @SerializedName("overview")
    var overview: String = "",
    @SerializedName("popularity")
    var popularity: Double = 0.0,
    @SerializedName("poster_path")
    var posterPath: String = "",

    @SerializedName("production_companies")
    var productionCompanies: List<ProductionCompany> = listOf(),
    @SerializedName("production_countries")
    var productionCountries: List<ProductionCountry> = listOf(),

    @SerializedName("release_date")
    var releaseDate: String = "",
    @SerializedName("revenue")
    var revenue: Int = 0,
    @SerializedName("runtime")
    var runtime: Int = 0,
    @SerializedName("spoken_languages")
    var spokenLanguages: List<SpokenLanguage> = listOf(),
    @SerializedName("status")
    var status: String = "",
    @SerializedName("tagline")
    var tagline: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("video")
    var video: Boolean = false,
    @SerializedName("vote_average")
    var voteAverage: Double = 0.0,
    @SerializedName("vote_count")
    var voteCount: Int = 0
)