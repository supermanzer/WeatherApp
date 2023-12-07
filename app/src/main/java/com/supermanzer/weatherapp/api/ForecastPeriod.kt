package com.supermanzer.weatherapp.api





class ForecastPeriod(
    val number: Number,
    val name: String,
    val isDaytime: Boolean,
    val temperature: Number,
    val temperatureUnit: String,
    val windSpeed: String,
    val windDirection: String,
    val icon: String,
    val shortForecast: String,
    val detailedForecast: String
)
