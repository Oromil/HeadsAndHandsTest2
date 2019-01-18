package com.oromil.hendsandheadstest.ui.main

import android.content.Context
import com.oromil.hendsandheadstest.R
import com.oromil.hendsandheadstest.data.entities.Weather

class WeatherMapper(private val context: Context) {

    fun mapToString(weather: Weather):String{
        return String.format(context.getString(R.string.weather_message), weather.name, weather.main.temp)
    }
}