package com.krishna.openweather.viewModels

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishna.myapplication.network.Service
import com.krishna.myapplication.network.ServiceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val services = MutableLiveData<List<Service>>()
    val success = MutableLiveData<Boolean>()


    fun getServices() {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val serviceResponse: ServiceResponse = repository.getServices()
                if (serviceResponse.success == "success") {
                    services.postValue(serviceResponse.services)
                    success.postValue(true)
                } else {
                    throw Exception()
                }
            }catch(e: Exception){
                success.postValue(false)
                e.printStackTrace()
            }

        }
    }


}