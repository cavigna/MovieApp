package com.cavigna.movieapp.model.models.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cavigna.movieapp.model.local.db.MovieDao
import com.cavigna.movieapp.model.models.model.movie.Movie
import com.cavigna.movieapp.model.remote.ApiService
import java.lang.Exception

class MoviePagingSource(
    private val api: ApiService,
    private val dao: MovieDao

) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        val page = params.key ?: 1
        return try {

            val response = api.fetchPopularMovies(page = page)

            val listApi = response.popularMovies

            dao.insertPopularMovies(listApi)
            LoadResult.Page(
                data = listApi,
                prevKey = if (page > 0) page - 1 else null,
                nextKey = if (response.popularMovies.isNotEmpty()) page + 1 else null
            )
        } catch (e: Exception) {
            Log.v("Paginación Fuente", e.message.toString())
            LoadResult.Error(e)


        }

    }
}
/*
catch (exception: HttpException) {
            Log.v("Paginación Fuente", "HttpException")
            LoadResult.Page(
                data = dao.selectPopularMovies(),
                prevKey = if (page > 0) page - 1 else null,
                nextKey = if (dao.selectPopularMovies().isNotEmpty()) page + 1 else null
            )
        }


catch (e: HttpException){
            val listDb = dao.selectPopularMovies()
            LoadResult.Page(

                data = listDb,
                prevKey = if (page > 0) page - 1 else null,
                nextKey = if (listDb.isNotEmpty()) page + 1 else null
            )
        }
 */
/*
return LoadResult.Page(
                data = response.popularMovies,
                prevKey = response.page+1,
                nextKey =response.page+1
            )
 */

/*
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let {anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)?: anchorPage?.nextKey?.minus(1)
        }
    }
 */