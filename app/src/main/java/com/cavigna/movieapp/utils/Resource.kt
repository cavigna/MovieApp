package com.cavigna.movieapp.utils

sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    class Success<T>(data: T): Resource<T>(data)
    class Loading<T>(data: T? = null): Resource<T>(data)
    class Error<T>(throwable: Throwable, data: T? = null): Resource<T>(data, throwable)
    class LocalDb<T>(data: T): Resource<T>(data)

    companion object{
        fun <T> success(data: T): Resource<T> = Success(data)
        fun <T> error(error: Throwable): Resource<T> = Error( error)
        fun <T> local (data: T): Resource<T> = LocalDb(data)


    }

}