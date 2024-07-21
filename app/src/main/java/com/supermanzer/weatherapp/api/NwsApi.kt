package com.supermanzer.weatherapp.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

private const val LAT = 36.603954
private const val LON = -121.898460
private const val FORECAST_URL = "https://api.weather.gov/gridpoints/MTR/92,50/forecast"

interface NwsApi {
    @GET("/")
    suspend fun fetchWeatherStatus(): String

    @GET(
        "/points/{LAT},{LON}"
    )
    suspend fun getForecastEndpoints(@Path("LAT") lat: Double, @Path("LON") lon: Double): Endpoints

    @GET()
    suspend fun getForecast(@Url url: String?): Forecast
}