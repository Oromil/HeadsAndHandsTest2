package com.oromil.hhtest.data.network

import com.oromil.hhtest.data.entities.WeatherEntity
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "http://api.openweathermap.org/"
private const val API_KEY = "c4b8f6f5ae94a0c7b188159ddda7c288"
private const val UNITS = "metric"

interface WeatherApi {

    @GET("data/2.5/weather?appid=$API_KEY&units=$UNITS")
    fun getWeather(@Query("lat") latitude: Float, @Query("lon") longitude: Float,
                   @Query("lang") language: String):Call<WeatherEntity>

    companion object Creator {
        fun create(): WeatherApi {
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
            return retrofit.create(WeatherApi::class.java)
        }
    }
}