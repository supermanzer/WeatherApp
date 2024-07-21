package com.supermanzer.weatherapp

import android.util.Log
import com.squareup.moshi.Moshi
import com.supermanzer.weatherapp.api.GeoInterceptor
import com.supermanzer.weatherapp.api.GeocodeApi
import com.supermanzer.weatherapp.api.GeocodeResponse
import com.supermanzer.weatherapp.api.NumberJsonAdapter
import com.supermanzer.weatherapp.api.NwsApi
import com.supermanzer.weatherapp.api.loggingInterceptor
import com.supermanzer.weatherapp.db.Location
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

private const val TAG="WeatherRepository"

val moshi = Moshi.Builder()
    .add(NumberJsonAdapter())
    .build()

class WeatherRepository {
    private val nwsApi: NwsApi
    private val geocodeApi: GeocodeApi

    init {
        val weatherClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.weather.gov")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(weatherClient)
            .build()
        nwsApi = retrofit.create(NwsApi::class.java)

        val geoHttpClient = OkHttpClient.Builder()
            .addInterceptor(GeoInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()

        val geoRetrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(geoHttpClient)
            .build()
        geocodeApi = geoRetrofit.create()
    }

    suspend fun fetchWeatherStatus() = nwsApi.fetchWeatherStatus()

    suspend fun fetchForecastEndpoints(lat: Double, lon: Double) = nwsApi.getForecastEndpoints(lat, lon)

    suspend fun getForecast(url: String) = nwsApi.getForecast(url)

    suspend fun getGeocodeResponse(location: String): GeocodeResponse {
        val response = geocodeApi.getGeocode(location)
        Log.d(TAG, "getGeocodeResponse: $response")
        return response
    }

}
