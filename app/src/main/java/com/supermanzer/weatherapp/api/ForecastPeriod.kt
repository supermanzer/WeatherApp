package com.supermanzer.weatherapp.api

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson


@JsonClass(generateAdapter = true)
data class ForecastPeriod(
    val number: Int,
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



class NumberJsonAdapter : JsonAdapter<Number>() {
    @FromJson
    override fun fromJson(reader: JsonReader): Number? {
        return when (reader.peek()) {
            JsonReader.Token.NUMBER -> reader.nextDouble()
            JsonReader.Token.STRING -> reader.nextString().toDouble()
            else -> null
        }
    }
    @ToJson
    override fun toJson(writer: JsonWriter, value: Number?) {
        writer.value(value)
    }
}