package com.supermanzer.weatherapp.db

import androidx.room.TypeConverter
import com.squareup.moshi.adapter
import com.supermanzer.weatherapp.api.ForecastPeriod
import com.supermanzer.weatherapp.moshi

class LocationTypeConverters {
    @TypeConverter
    fun fromInt(int: Int): Boolean {
        return int > 0
    }

    @TypeConverter
    fun toInt(boolean: Boolean): Int {
        return if(boolean) 1 else 0
    }

    @OptIn(ExperimentalStdlibApi::class)
    @TypeConverter
    fun fromForecastPeriodList(periods: List<ForecastPeriod>): String =
        moshi.adapter<List<ForecastPeriod>>().toJson(periods)

    @OptIn(ExperimentalStdlibApi::class)
    @TypeConverter
    fun toForecastPeriodList(json: String): List<ForecastPeriod> =
        moshi.adapter<List<ForecastPeriod>>().fromJson(json) ?: emptyList()
}