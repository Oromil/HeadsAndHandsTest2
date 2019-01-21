package com.oromil.hhtest.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherEntity(
        @SerializedName("weather")
        @Expose
        var weather: List<WeatherType>,
        @SerializedName("main")
        @Expose
        var main: Main,
        @SerializedName("name")
        @Expose
        var name: String) {

    data class Main(
            @SerializedName("temp")
            @Expose
            var temp: Double
    )

    data class WeatherType(
            @SerializedName("main")
            @Expose
            var main: String,
            @SerializedName("description")
            @Expose
            var description: String
    )
}