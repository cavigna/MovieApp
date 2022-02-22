package com.cavigna.movieapp.model

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.cavigna.movieapp.model.local.db.BaseDeDatos
import com.cavigna.movieapp.model.local.db.MovieDao
import com.cavigna.movieapp.model.models.model.movie.MovieResponse
import com.cavigna.movieapp.model.models.model.movie.MovieDetail
import com.cavigna.movieapp.model.models.paging.MoviePagingSource
import com.cavigna.movieapp.model.remote.ApiService
import com.cavigna.movieapp.utils.Resource
import com.cavigna.movieapp.utils.networkBoundResource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class Repositorio @Inject constructor(
    private val api: ApiService,
    private val dao: MovieDao,
) {


    suspend fun selectFavoriteBooks() = flowOf(dao.selectFavorteMovieDetail())


    suspend fun fetchSearchMovieResource(query: String) = flow {
        emit(Resource.success(api.fetchSearchMovies(query = query)))
    }.catch { e ->
        Resource.error<MovieResponse>(e)
    }

    suspend fun fetchUpComingMovies() = flow {
        emit(Resource.success(api.fetchUpcomingMovies()))
    }.catch { e ->
        Resource.error<MovieResponse>(e)
    }

    suspend fun updateMovieDetailFavorite(movie: MovieDetail) =
        dao.updateMovieDetailsFavorite(movie)

    suspend fun fetchImagesDetail(id: Int) = api.fetchImages(id)

    fun selectPopularMovies() = flow {
        emit(dao.selectPopularMovies())
    }


    fun selectMovieDetailCached(id: Int) = networkBoundResource(
        query = { dao.selectMovieDetailFlow(id) },
        fetch = { api.fetchMovieDetail(id) },
        saveFetchResult = { dao.insertMovieDetail(api.fetchMovieDetail(id)) },
        coroutineDispatcher = IO
    )

    @OptIn(ExperimentalPagingApi::class)
    val listadoMoviesPager = Pager(
        config = PagingConfig(1),
    ) {
        MoviePagingSource(api, dao)
    }.flow


}

/*
@OptIn(ExperimentalPagingApi::class)
    val listadoMoviesPager = Pager(
        config = PagingConfig(1),
        //remoteMediator = MovieRemoteMediator2(api, db),
        // initialKey = 1,
        //pagingSourceFactory = MoviePagingSource(dao, api)
    ) {
        //MovieRemoteMediator2(api, db)
        MoviePagingSource(api, dao)

    }.flow
 */