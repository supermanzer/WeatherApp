package com.supermanzer.weatherapp.db

import androidx.room.Entity
import com.supermanzer.weatherapp.api.ForecastPeriod

const val FORECAST_TYPE_DAILY = "daily"
const val FORECAST_TYPE_HOURLY = "hourly"

@Entity(tableName = "cached_forecast", primaryKeys = ["locationId", "type"])
data class CachedForecastEntity(
    val locationId: String,
    val type: String,
    val periods: List<ForecastPeriod>,
    val fetchedAt: Long
)
