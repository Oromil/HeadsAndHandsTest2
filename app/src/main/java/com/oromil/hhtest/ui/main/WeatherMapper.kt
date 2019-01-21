package com.oromil.hhtest.ui.main

import android.content.Context
import com.oromil.hhtest.R
import com.oromil.hhtest.data.entities.WeatherEntity
import java.lang.StringBuilder

class WeatherMapper(private val context: Context) {

    fun mapToString(weather: WeatherEntity): String = buildString {
        append(String.format(context.getString(R.string.weather_message), weather.name, weather.main.temp))
                .append(" ")
        val descriptions = weather.weather
        for (i in 0 until descriptions.size) {
            append(descriptions[i].description)
            append(if (i == descriptions.lastIndex) "." else ",")
        }
    }
}