package com.krishna.openweather.models

import com.google.gson.annotations.SerializedName


data class WeatherResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<Forecast>,
    val city: City
)

data class Forecast(
    val dt: Long,
    val main: Main
)

data class Main(
    val temp: Double,

    @SerializedName("feels_like")
    val feelsLike: Double,

    @SerializedName("temp_min")
    val tempMin: Double,

    @SerializedName("temp_max")
    val tempMax: Double,
    val pressure: Int,

    @SerializedName("sea_level")
    val seaLevel: Int,

    @SerializedName("grnd_level")
    val grndLevel: Int,
    val humidity: Int,

    @SerializedName("temp_kf")
    val tempKf: Double
)

data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)

data class Coord(
    val lat: Double,
    val lon: Double
)
