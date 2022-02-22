package com.cavigna.movieapp.utils

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.cavigna.movieapp.model.models.model.credits.Cast
import com.cavigna.movieapp.model.models.model.credits.Crew
import com.cavigna.movieapp.model.models.model.details.Genre
import com.cavigna.movieapp.model.models.model.details.ProductionCompany
import com.cavigna.movieapp.model.models.model.details.ProductionCountry
import com.cavigna.movieapp.model.models.model.details.SpokenLanguage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@TypeConverters
class Converters {

    @TypeConverter
    fun fromListadoIntToString(list: List<Int>):String{
        return list.joinToString {
            it.toString()
        }
    }

    @TypeConverter
    fun fromStringToListadoInt(string: String?):List<Int?>?{
        val numeros = string?.split(",")

        return numeros?.map { it.toIntOrNull() }
    }




    @TypeConverter
    fun fromProdCompany(json:String):List<ProductionCompany>{
        val type=object : TypeToken<List<ProductionCompany>>(){}.type
        return Gson().fromJson(json,type)
    }
    @TypeConverter
    fun toProdCompany(productionCompanies:List<ProductionCompany>):String{

        val type =object : TypeToken<List<ProductionCompany>>(){}.type

        return Gson().toJson(productionCompanies,type)?: "[]"
    }


    @TypeConverter
    fun fromProdCountry(productionCountry: List<ProductionCountry>): String{

        val type : Type = object : TypeToken<List<ProductionCountry>>(){}.type

        return Gson().toJson(productionCountry,type)?: "[]"
    }


    @TypeConverter
    fun toProdCountry(json: String):List<ProductionCountry>{
        val type = object  : TypeToken<List<ProductionCountry>>(){}.type

        return Gson().fromJson(json, type)
    }


    @TypeConverter
    fun fromSpokenLan(spokenLanguage: List<SpokenLanguage>):String{
        val type = object : TypeToken<List<SpokenLanguage>>(){}.type

        return Gson().toJson(spokenLanguage, type)?: "[]"
    }


    @TypeConverter
    fun toSpokenLan(json:String):List<SpokenLanguage>{
        val type=object : TypeToken<List<SpokenLanguage>>(){}.type
        return Gson().fromJson(json,type)
    }


    @TypeConverter
    fun fromGenre(listGenres: List<Genre>):String{
        val type = object : TypeToken<List<Genre>>(){}.type

        return Gson().toJson(listGenres, type)?: "[]"
    }


    @TypeConverter
    fun toSGenre(json:String):List<Genre>{
        val type=object : TypeToken<List<Genre>>(){}.type
        return Gson().fromJson(json,type)
    }

    @TypeConverter
    fun fromCast(listGenres: List<Cast>):String{
        val type = object : TypeToken<List<Cast>>(){}.type

        return Gson().toJson(listGenres, type)?: "[]"
    }

    @TypeConverter
    fun toCast(json:String):List<Cast>{
        val type=object : TypeToken<List<Cast>>(){}.type
        return Gson().fromJson(json,type)
    }

    @TypeConverter
    fun fromCrew(listGenres: List<Crew>):String{
        val type = object : TypeToken<List<Genre>>(){}.type

        return Gson().toJson(listGenres, type)?: "[]"
    }


    @TypeConverter
    fun toCrew(json:String):List<Crew>{
        val type=object : TypeToken<List<Crew>>(){}.type
        return Gson().fromJson(json,type)
    }



}