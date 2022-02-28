package com.cavigna.movieapp.model.local.db

import androidx.paging.PagingSource
import androidx.room.*
import com.cavigna.movieapp.model.models.model.images.ImagesResponse
import com.cavigna.movieapp.model.models.model.movie.Movie
import com.cavigna.movieapp.model.models.model.movie.MovieDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPopularMovies(listMovie: List<Movie>)

    @Query("DELETE FROM movies_popular_table")
    suspend fun deletePopularMovies()

    @Query("SELECT * FROM movies_popular_table")
    fun selectPopularMovies(): List<Movie>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetail(movieDetail: MovieDetail)

    @Update
    suspend fun updateMovieDetailsFavorite(movieDetail: MovieDetail)

    @Query("SELECT * FROM movie_details_table WHERE id =:id")
    suspend fun selectMovieDetail(id:Int): MovieDetail

    @Query("SELECT * FROM movies_popular_table")
    fun selectMoviesWIthPaging(): PagingSource<Int, Movie>

    @Query("SELECT * FROM movie_details_table WHERE favorite =1")
    suspend fun selectFavorteMovieDetail(): List<MovieDetail>

    /* PRUEBAS */

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPopularMovieOneOnly(movie: Movie)

    @Query("SELECT * FROM movie_details_table WHERE id =:id")
     fun selectMovieDetailFlow(id:Int): Flow<MovieDetail>


     /* SEARCH */

     @Query("SELECT * FROM movies_popular_table WHERE searched=1")
     fun selectMovieSearch(): Flow<List<Movie>>

    /**
     * IMAGES QUERY
     *
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(imagesResponse: ImagesResponse)

    @Query("SELECT * FROM images_details_table WHERE id =:id")
    suspend fun selectImages(id:Int):ImagesResponse
}