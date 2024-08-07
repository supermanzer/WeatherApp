package com.supermanzer.weatherapp.api

import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodeApi {
//        @GET("/")
//    suspend fun getGeocode(@Query("address") string: String) : GeocodeResponse
    @GET("maps/api/geocode/json")
    suspend fun getGeocode(@Query("address") string: String): GeocodeResponse

}