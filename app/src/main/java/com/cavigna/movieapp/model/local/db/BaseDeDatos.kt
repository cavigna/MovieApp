package com.cavigna.movieapp.model.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cavigna.movieapp.model.models.model.images.ImagesResponse
import com.cavigna.movieapp.model.models.model.movie.Movie
import com.cavigna.movieapp.model.models.model.movie.MovieDetail
import com.cavigna.movieapp.utils.Converters

@TypeConverters(Converters::class)
@Database(
    entities = [Movie::class, MovieDetail::class, ImagesResponse::class],
    version = 1,
    exportSchema = false,


)
abstract class BaseDeDatos: RoomDatabase() {
    abstract val dao : MovieDao
}