package com.cavigna.movieapp.model.models.paging

import androidx.paging.*
import androidx.room.withTransaction
import com.cavigna.movieapp.model.local.db.BaseDeDatos
import com.cavigna.movieapp.model.models.model.movie.Movie
import com.cavigna.movieapp.model.models.model.movie.MovieResponse
import com.cavigna.movieapp.model.remote.ApiService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val api: ApiService,
    private val db: BaseDeDatos,

    ) : RemoteMediator<Int, Movie>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {
        val dao = db.dao
        val page = 1
        return try {
            val response = api.fetchPopularMovies(page = page)
            val movies = response.popularMovies

            dao.insertPopularMovies(movies)

            val endOfPaginationReached = movies.isEmpty()
            MediatorResult.Success(
                endOfPaginationReached = endOfPaginationReached
            )

        } catch (e: IOException) {
            MediatorResult.Error(e)

        }

    }
}


@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator2(
    private val api: ApiService,
    private val db: BaseDeDatos,
) : RemoteMediator<Int, Movie>() {
    private val dao = db.dao

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {

        return try {
            /**
             *The network load method takes an optional after=<user.id>
             * parameter. For every page after the first, pass the last user
             *ID to let it continue from where it left off. For REFRESH,
             * pass null to load the first page.
             */
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val pageApi = getKeyClosest(state)?.page?.minus(1)?:1
                }
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND -> {

                    val pageApi = getKeyForFirstItem(state).page
                    if (pageApi == null){
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }
                    pageApi


                }
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()

                    // You must explicitly check if the last item is null when
                    // appending, since passing null to networkService is only
                    // valid for initial load. If lastItem is null it means no
                    // items were loaded after the initial REFRESH and there are
                    // no more items to load.
                    if (lastItem == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }

                    lastItem.id
                }
            }

            // Suspending network load via Retrofit. This doesn't need to be
            // wrapped in a withContext(Dispatcher.IO) { ... } block since
            // Retrofit's Coroutine CallAdapter dispatches on a worker
            // thread.
            val response = api.fetchPopularMovies(page = page as Int)

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao.deletePopularMovies()
                }

                // Insert new users into database, which invalidates the
                // current PagingData, allowing Paging to present the updates
                // in the DB.
                dao.insertPopularMovies(response.popularMovies)
            }

            MediatorResult.Success(
                endOfPaginationReached = response.popularMovies.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyForFirstItem(state: PagingState<Int, Movie>): MovieResponse {
        return state.pages.firstOrNull{ it.data.isNotEmpty()}?.data?.firstOrNull().let {
            api.fetchPopularMovies()
        }
    }

    private suspend fun  getKeyClosest(state: PagingState<Int, Movie>): MovieResponse?{
        return state.anchorPosition?.let { position->
            state.closestItemToPosition(position)?.let { id->
                api.fetchPopularMovies()
            }
        }
    }


}


/*
@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator2(
    private val api: ApiService,
    private val db: BaseDeDatos,
) : RemoteMediator<Int, Movie>() {
    private val dao = db.dao

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {

        return try {
            /**
             *The network load method takes an optional after=<user.id>
             * parameter. For every page after the first, pass the last user
             *ID to let it continue from where it left off. For REFRESH,
             * pass null to load the first page.
             */
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()

                    // You must explicitly check if the last item is null when
                    // appending, since passing null to networkService is only
                    // valid for initial load. If lastItem is null it means no
                    // items were loaded after the initial REFRESH and there are
                    // no more items to load.
                    if (lastItem == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }

                    lastItem.id
                }
            }

            // Suspending network load via Retrofit. This doesn't need to be
            // wrapped in a withContext(Dispatcher.IO) { ... } block since
            // Retrofit's Coroutine CallAdapter dispatches on a worker
            // thread.
            val response = api.fetchPopularMovies(page = loadKey ?: 1)

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao.deletePopularMovies()
                }

                // Insert new users into database, which invalidates the
                // current PagingData, allowing Paging to present the updates
                // in the DB.
                dao.insertPopularMovies(response.popularMovies)
            }

            MediatorResult.Success(
                endOfPaginationReached = response.popularMovies.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyForFirstItem(state: PagingState<Int, Movie>): MovieResponse {
        return state.pages.firstOrNull{ it.data.isNotEmpty()}?.data?.firstOrNull().let {
            api.fetchPopularMovies()
        }
    }


}
 */