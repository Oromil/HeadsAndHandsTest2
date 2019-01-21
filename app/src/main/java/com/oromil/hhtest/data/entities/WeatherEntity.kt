package com.oromil.hhtest.data.entities

import com.google.gson.annotations.SerializedName

data class WeatherEntity(
        @SerializedName("weather")
        var weather: List<WeatherType>,
        @SerializedName("main")
        var main: Main,
        @SerializedName("name")
        var name: String) {

    data class Main(
            @SerializedName("temp")
            var temp: Double
    )

    data class WeatherType(
            @SerializedName("main")
            var main: String,
            @SerializedName("description")
            var description: String
    )
}