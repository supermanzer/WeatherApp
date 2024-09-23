package com.supermanzer.weatherapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Location::class], version = 1, exportSchema = false)
@TypeConverters(LocationTypeConverters::class)
abstract class LocationDatabase: RoomDatabase() {
    abstract fun locationDao(): LocationDao
}