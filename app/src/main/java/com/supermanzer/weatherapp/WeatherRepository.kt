package com.supermanzer.weatherapp

import com.supermanzer.weatherapp.api.NwsApi
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create



class WeatherRepository {
    private val nwsApi: NwsApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.weather.gov")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        nwsApi = retrofit.create()
    }

    suspend fun fetchWeatherStatus() = nwsApi.fetchWeatherStatus()

    suspend fun fetchForecastEndpoints() = nwsApi.getForecastEndpoints()

    suspend fun getForecast() = nwsApi.getForecast()
}