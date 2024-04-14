package com.krishna.openweather.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.krishna.openweather.network.WeatherAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun weatherAPI(): WeatherAPI {
        val key= com.krishna.openweather.BuildConfig.API_KEY

        val gson: Gson = GsonBuilder().setLenient().create()

        val weatherAPI: WeatherAPI by lazy {
            val httpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val originalHttpUrl = original.url()

                    val url = originalHttpUrl.newBuilder()
                        .addQueryParameter("appid", key)
                        .build()

                    val requestBuilder = original.newBuilder()
                        .url(url)

                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            retrofit.create(WeatherAPI::class.java)
        }

        return weatherAPI
    }


}