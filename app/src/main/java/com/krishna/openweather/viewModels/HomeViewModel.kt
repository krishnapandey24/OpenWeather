package com.krishna.openweather.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishna.openweather.models.WeatherResponse
import com.krishna.openweather.repositories.HomeRepository
import com.krishna.openweather.utils.Appt.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {
    val weatherResponse = MutableLiveData<WeatherResponse>()
    val success = MutableLiveData<Int>()

    fun getWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response: WeatherResponse = repository.getWeatherByCoOrdinates(latitude, longitude)
                weatherResponse.postValue(response)
                success.postValue(1)
            } catch (e: Exception) {
                success.postValue(0)
                e.printStackTrace()
            }
        }
    }


}