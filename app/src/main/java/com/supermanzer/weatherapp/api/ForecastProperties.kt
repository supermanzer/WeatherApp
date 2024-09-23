package com.supermanzer.weatherapp.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ForecastProperties(
    val periods: List<ForecastPeriod>
)
