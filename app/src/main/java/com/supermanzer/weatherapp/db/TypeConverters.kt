package com.supermanzer.weatherapp.db

import androidx.room.TypeConverter

class LocationTypeConverters {
    @TypeConverter
    fun fromInt(int: Int): Boolean {
        return int > 0
    }

    @TypeConverter
    fun toInt(boolean: Boolean): Int {
        return if(boolean) 1 else 0
    }
}