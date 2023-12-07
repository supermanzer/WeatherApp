package com.supermanzer.weatherapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Location(
    @PrimaryKey val id: UUID,
    val name: String,
    val lat: Number?,
    val lon: Number?,
    val forecastUrl: String?,
    val forecastHourlyUrl: String?,
)
