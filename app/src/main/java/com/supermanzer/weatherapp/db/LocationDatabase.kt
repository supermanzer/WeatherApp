package com.supermanzer.weatherapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Location::class, CachedForecastEntity::class], version = 3, exportSchema = false)
@TypeConverters(LocationTypeConverters::class)
abstract class LocationDatabase: RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun forecastCacheDao(): ForecastCacheDao

    companion object
}