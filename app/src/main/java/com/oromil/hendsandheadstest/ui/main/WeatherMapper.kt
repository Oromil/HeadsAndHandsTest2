package com.oromil.hendsandheadstest.ui.main

import android.content.Context
import com.oromil.hendsandheadstest.R
import com.oromil.hendsandheadstest.data.entities.WeatherEntity
import java.lang.StringBuilder

class WeatherMapper(private val context: Context) {

    fun mapToString(weather: WeatherEntity): String {
        val builder = StringBuilder()
        builder.append(String.format(context.getString(R.string.weather_message), weather.name,
                weather.main.temp)).append(" ")
        val descriptions = weather.weather
        for (i in 0 until descriptions.size) {
            builder.append(descriptions[i].description)
            if (i == descriptions.lastIndex)
                builder.append(".")
            else
                builder.append(",")
        }
        return builder.toString()
    }
}