package com.cavigna.movieapp.model.remote

import com.cavigna.movieapp.BuildConfig
import com.cavigna.movieapp.model.models.model.credits.CreditResponse
import com.cavigna.movieapp.model.models.model.images.ImagesResponse
import com.cavigna.movieapp.model.models.model.movie.MovieDetail
import com.cavigna.movieapp.model.models.model.movie.MovieResponse
import com.cavigna.movieapp.model.models.model.rating.GuestSession
import com.cavigna.movieapp.model.models.model.rating.RespuestaPostRating
import com.google.gson.JsonObject
import retrofit2.http.*


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

    //https://api.themoviedb.org/3/authentication/guest_session/new?api_key=
    @GET("authentication/guest_session/new")
    suspend fun fetchGuestSession(
        @Query(value= "api_key") apiKey:String = APIKEY,
    ): GuestSession

    //https://api.themoviedb.org/3/movie/107/rating?api_key=...&guest_session_id=...
    @POST("movie/{id}/rating")
    suspend fun postRating(
        @Path("id")id:Int,
        @Body rating : JsonObject,
        @Query("guest_session_id") guestSessionId: String,
        @Query(value= "api_key") apiKey:String = APIKEY,

    ): RespuestaPostRating

}





