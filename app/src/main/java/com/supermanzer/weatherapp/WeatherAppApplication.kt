package com.supermanzer.weatherapp

import android.app.Application
import com.supermanzer.weatherapp.db.LocationRepository

class WeatherAppApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        LocationRepository.initialize(this)
    }
}