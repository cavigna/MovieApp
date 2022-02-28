package com.cavigna.movieapp.ui.states

import androidx.paging.PagingData
import com.cavigna.movieapp.model.models.model.images.ImagesResponse
import com.cavigna.movieapp.model.models.model.movie.Movie
import com.cavigna.movieapp.model.models.model.movie.MovieResponse
import com.cavigna.movieapp.model.models.model.movie.MovieDetail
import com.cavigna.movieapp.utils.Resource


sealed class UiHomeState {
    data class Success(val movieResponse: MovieResponse?) : UiHomeState()
    data class Local(val movies: List<Movie>) : UiHomeState()
    object Loading : UiHomeState()
    data class Error(val message: String, val moviesPopuular: List<Movie>)
    data class Prueba(val pagingData: PagingData<Movie>) : UiHomeState()
}

sealed class UiDetailsState {
    object Loading: UiDetailsState()
    //data class Success( val movieDetail: MovieDetail?): UiDetailsState()
    data class Success( val movieDetail: MovieDetail?, val imagesResponse: ImagesResponse?): UiDetailsState()
    data class Error(val e: Throwable?): UiDetailsState()

}

sealed class UiSearchState {
    data class Success(val movieResponse: MovieResponse) : UiSearchState()
    object Loading : UiSearchState()
    object Empty : UiSearchState()
    data class Error(val e:Throwable?,  val message: String) : UiSearchState()


}

sealed class UiUpComingState {
    data class Success(val movieResponse: MovieResponse) : UiUpComingState()
    object Loading : UiUpComingState()
    data class Error(val e:Throwable?,  val message: String) : UiUpComingState()

}

sealed class UiFavoriteState{
    data class Success(val listOfFavorites: List<MovieDetail>):UiFavoriteState()
    data class Error(val e:Throwable?,  val message: String) : UiFavoriteState()
    object  Loading : UiFavoriteState()

    companion object{
        fun success( listOfFavorites: List<MovieDetail>) = listOfFavorites
    }
}