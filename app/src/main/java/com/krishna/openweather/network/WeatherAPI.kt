package com.krishna.openweather.network

import com.krishna.openweather.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("forecast?units=metric")
    suspend fun getWeatherForecast(@Query("lat") latitude: Double, @Query("lon") longitude: Double): WeatherResponse




}