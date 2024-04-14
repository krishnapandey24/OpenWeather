package com.krishna.openweather.repositories


import com.krishna.openweather.models.WeatherResponse
import com.krishna.openweather.network.WeatherAPI
import javax.inject.Inject

class HomeRepository @Inject constructor(private val weatherAPI: WeatherAPI) {


    suspend fun getWeatherByCoOrdinates(latitude: Double, longitude: Double): WeatherResponse {
        return weatherAPI.getWeatherForecast(latitude, longitude)
    }


}