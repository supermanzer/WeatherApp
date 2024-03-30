package com.supermanzer.weatherapp

import com.supermanzer.weatherapp.api.GeocodeApi
import com.supermanzer.weatherapp.api.NwsApi
import com.supermanzer.weatherapp.db.Location
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

private val geoApiKey = BuildConfig.MAPS_API_KEY


class WeatherRepository {
    private val nwsApi: NwsApi
//    private val geocodeApi: GeocodeApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.weather.gov")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        nwsApi = retrofit.create()

//        val geoRetrofit: Retrofit = Retrofit.Builder()
//            .baseUrl("https://maps.googleapis.com/maps/api/geocode/json")
//            .addConverterFactory(MoshiConverterFactory.create())
//            .build()
//        geocodeApi = geoRetrofit.create()
    }

    suspend fun fetchWeatherStatus() = nwsApi.fetchWeatherStatus()

    suspend fun fetchForecastEndpoints() = nwsApi.getForecastEndpoints()

    suspend fun getForecast(url: String) = nwsApi.getForecast(url)

//    suspend fun getGeocodeResponse(location: String) = geocodeApi.getGeocode(location)
}
