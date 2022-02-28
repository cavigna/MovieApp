package com.cavigna.movieapp.app

import android.content.Context
import androidx.room.Room
import com.cavigna.movieapp.model.local.db.BaseDeDatos
import com.cavigna.movieapp.model.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.google.gson.GsonBuilder

import com.google.gson.Gson




@Module
@InstallIn(SingletonComponent::class)
object DataModule {


    var gson = GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .create()

    @Provides
    @Singleton
    fun providesRetrofit(): ApiService = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService::class.java)

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, BaseDeDatos::class.java, "movie.db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providesMovieDao(db: BaseDeDatos) = db.dao

}