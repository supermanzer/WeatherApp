package com.supermanzer.weatherapp.api

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class NwsGeometry (
    val type: String,
    val coordinates: Array<Number>
)

@JsonClass(generateAdapter = true)
data class NwsProperties (
    val forecast: String,
    val forecastHourly: String,
)
@JsonClass(generateAdapter = true)
data class Endpoints(
    val geometry: NwsGeometry,
    val properties: NwsProperties
)

