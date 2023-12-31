package com.supermanzer.weatherapp.api

import okhttp3.ResponseBody
import retrofit2.http.GET

private const val LAT = 36.603954
private const val LON = -121.898460
private const val FORECAST_URL = "https://api.weather.gov/gridpoints/MTR/92,50/forecast"

interface NwsApi {
    @GET("/")
    suspend fun fetchWeatherStatus(): String

    @GET(
        "/points/$LAT,$LON"
    )
    suspend fun getForecastEndpoints(): String

    @GET(FORECAST_URL)
    suspend fun getForecast(): ResponseBody
}