package com.krishna.openweather.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.krishna.openweather.network.WeatherAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun weatherAPI(): WeatherAPI {
        val url = "https://api.openweathermap.org/data/2.5/"
        val gson: Gson = GsonBuilder().setLenient().create()

        val weatherAPI: WeatherAPI by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            retrofit.create(WeatherAPI::class.java)
        }

        return weatherAPI
    }


}