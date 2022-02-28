package com.cavigna.movieapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.cavigna.movieapp.model.Repositorio
import com.cavigna.movieapp.model.models.model.images.ImagesResponse
import com.cavigna.movieapp.model.models.model.movie.MovieDetail
import com.cavigna.movieapp.ui.states.*
import com.cavigna.movieapp.utils.Resource
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: Repositorio) : ViewModel() {

    init {

        fetchUpcomingMovies()
        selectFavoriteMovies()


    }

    private val _uiHomeState = MutableStateFlow<UiHomeState>(UiHomeState.Loading)
    val uiHomeState: Flow<UiHomeState> = _uiHomeState

    private val _uiDetailState = MutableStateFlow<UiDetailsState>(UiDetailsState.Loading)
    val uiDetailsState: Flow<UiDetailsState> = _uiDetailState

    private val _uiSearchState = MutableStateFlow<UiSearchState>(UiSearchState.Empty)
    val uiSearchState: Flow<UiSearchState> = _uiSearchState

    private val _uiUpComingState = MutableStateFlow<UiUpComingState>(UiUpComingState.Loading)
    val uiUpComingState: Flow<UiUpComingState> = _uiUpComingState

    private val _uiFavoriteState = MutableStateFlow<UiFavoriteState>(UiFavoriteState.Loading)
    val uiFavoriteState: Flow<UiFavoriteState> = _uiFavoriteState

    val idMovie = MutableStateFlow(1)

    val moviesPager = repo.listadoMoviesPager.cachedIn(viewModelScope)


    fun selectFavoriteMovies() {
        viewModelScope.launch(IO) {
            repo.selectFavoriteMovies().collect {
                _uiFavoriteState.value = UiFavoriteState.Success(it)
            }
        }
    }

    fun selectMovie() {
        viewModelScope.launch(IO) {
            repo.selectPopularMovies().collect {
                _uiHomeState.value = UiHomeState.Local(it)
            }

        }
    }

    fun updateMovieDetail(movieDetail: MovieDetail) {
        viewModelScope.launch(IO) {
            movieDetail.favorite = !movieDetail.favorite
            repo.updateMovieDetailFavorite(movieDetail)
        }
    }


    fun fetchUpcomingMovies() {
        viewModelScope.launch(IO) {
            repo.fetchUpComingMovies().collect { resource ->
                when (resource) {
                    is Resource.Success -> _uiUpComingState.value =
                        UiUpComingState.Success(resource.data!!)
                    is Resource.Loading -> _uiUpComingState.value = UiUpComingState.Loading
                    is Resource.Error -> _uiUpComingState.value =
                        UiUpComingState.Error(resource.error!!, "Hubo un Error")
                }
            }
        }
    }


    fun fetchSearchMovie(query: String) {
        viewModelScope.launch(IO) {
            repo.fetchSearchMovieResource(query = query).collect { resource ->
                when (resource) {
                    is Resource.Success -> _uiSearchState.value =
                        UiSearchState.Success(resource.data!!)
                    is Resource.Loading -> _uiSearchState.value = UiSearchState.Loading
                    is Resource.Error -> _uiSearchState.value =
                        UiSearchState.Error(resource.error!!, "Hubo un Error")
                }
            }
        }
    }


    fun selectMovieDetails(id: Int = idMovie.value) {
        viewModelScope.launch(IO) {
            _uiDetailState.value = UiDetailsState.Success(MovieDetail(), ImagesResponse())
            repo.selectMovieDetailCached(id).collect { resource ->
                repo.fetchOrSelectImages(id).collect {imageResponse: ImagesResponse ->

                    when (resource) {
                        is Resource.Success -> _uiDetailState.value =
                            UiDetailsState.Success(resource.data, imageResponse)
                        is Resource.Loading -> _uiDetailState.value = UiDetailsState.Loading
                        is Resource.Error -> _uiDetailState.value =
                            UiDetailsState.Error(resource.error)
                    }
                }
            }
        }
    }


    val puntaje: MutableLiveData<Double> = MutableLiveData(0.0)
    fun postRating(id: Int, rating: Double = puntaje.value!!) {
        viewModelScope.launch(IO) {


            val guestSessionId =
                withContext(this.coroutineContext) { repo.fetchGuestSession() }
            Log.i("post", guestSessionId.toString())

            if (puntaje.value!! > 0.5 && guestSessionId != null) {
                val jsonObjectRating = JsonObject()
                jsonObjectRating.addProperty("value", puntaje.value)
                repo.postRating(id, jsonObjectRating, guestSessionId.guestSessionId).also {
                    Log.i("post", it.toString())
                }
            }
        }
    }


}



