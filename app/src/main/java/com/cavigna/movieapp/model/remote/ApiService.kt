package com.cavigna.movieapp.model.remote

import com.cavigna.movieapp.BuildConfig
import com.cavigna.movieapp.model.models.model.movie.MovieResponse
import com.cavigna.movieapp.model.models.model.movie.MovieDetail
import com.cavigna.movieapp.model.models.model.credits.CreditResponse
import com.cavigna.movieapp.model.models.model.images.ImagesResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


//https://image.tmdb.org/t/p/w500/EnDlndEvw6Ptpp8HIwmRcSSNKQ.jpg
//https://image.tmdb.org/t/p/w500/iQFcwSGbZXMkeyKrxbPnwnRo5fl.jpg

//https://image.tmdb.org/t/p/original/EnDlndEvw6Ptpp8HIwmRcSSNKQ.jpg


interface ApiService {
    /**
     * Si querés usar la app, y porbar el código, cambiá el valor de acá abajo. Reemplazá la
     * [APIKEY] por tu APIKEY.
     * Si no, la app no te va a compilar!
     */

        companion object{
            const val APIKEY = BuildConfig.API_KEY
        }

    @GET("movie/popular")
    suspend fun fetchPopularMovies(
        @Query(value= "api_key") apiKey:String = APIKEY,
        @Query(value= "language") language:String = "en-Us",
        @Query(value= "page") page:Int = 1
    ): MovieResponse

    @GET("movie/{id}")
    suspend fun fetchMovieDetail(
        @Path("id")id:Int,
        @Query(value= "api_key") apiKey:String = APIKEY,
        @Query(value= "language") language:String = "en-Us",
        @Query(value= "page") page:Int = 1
    ): MovieDetail

    @GET("search/movie")
    suspend fun fetchSearchMovies(
        @Query(value= "api_key") apiKey:String = APIKEY,
        @Query(value= "language") language:String = "en-Us",
        @Query(value= "page") page:Int = 1,
        @Query(value = "query", encoded = true) query:String
    ): MovieResponse

    @GET("movie/upcoming")
    suspend fun fetchUpcomingMovies(
        @Query(value= "api_key") apiKey:String = APIKEY,
        @Query(value= "language") language:String = "en-Us",
        @Query(value= "page") page:Int = 1
    ): MovieResponse


    @GET("movie/{id}/credits")
    suspend fun fetchCredits(
        @Path("id")id:Int,
        @Query(value= "api_key") apiKey:String = APIKEY,
        @Query(value= "language") language:String = "en-Us",
        @Query(value= "page") page:Int = 1
    ): CreditResponse

    @GET("movie/{id}/images")
    suspend fun fetchImages(
        @Path("id")id:Int,
        @Query(value= "api_key") apiKey:String = APIKEY,
        @Query(value= "language") language:String = "en-Us",
        @Query(value= "page") page:Int = 1,
        @Query(value = "include_image_language") includeLanguages: String ="en"
    ): ImagesResponse

//    @POST("movie/{movie_id}/rating")
//    suspend fun ()
}