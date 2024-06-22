package com.supermanzer.weatherapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermanzer.weatherapp.api.Forecast
import com.supermanzer.weatherapp.api.ForecastPeriod
import com.supermanzer.weatherapp.api.ForecastProperties
import com.supermanzer.weatherapp.db.Location
import com.supermanzer.weatherapp.db.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

private const val TAG = "ForecastViewModel"

class ForecastViewModel: ViewModel() {
    private val weatherRepository = WeatherRepository()
    private val locationRepository = LocationRepository.get()
    var apiError: Boolean = false
    var apiErrorMessage: String? = null

    private val _hourlyForecastPeriods: MutableStateFlow<List<ForecastPeriod>>  = MutableStateFlow(
        emptyList())
    private val _forecastPeriods: MutableStateFlow<List<ForecastPeriod>> =
        MutableStateFlow(emptyList())
    val forecastPeriods: StateFlow<List<ForecastPeriod>>
        get() = _forecastPeriods.asStateFlow()
    val hourlyForecastPeriods: StateFlow<List<ForecastPeriod>>
        get() = _hourlyForecastPeriods.asStateFlow()



    private suspend fun setForecastPeriods(location: Location) {
        if (location.forecastUrl !== null) {
            val url = location.forecastUrl
            fetchUrlSetState(url, _forecastPeriods)
        } else {
            apiError = true
            apiErrorMessage = "Forecast URL not set for location ${location.name}"
        }
    }
    private suspend fun setHourlyForecast(location: Location) {
        if (location.forecastHourlyUrl !== null) {
            val url = location.forecastHourlyUrl
            fetchUrlSetState(url, _hourlyForecastPeriods)
        } else {
            apiError = true
            apiErrorMessage = "Hourly forecast URL not set for location ${location.name}"
        }
    }

    private suspend fun fetchUrlSetState(url: String, stateFlow: MutableStateFlow<List<ForecastPeriod>>){
        val forecast: Forecast = weatherRepository.getForecast(url)
        Log.d(TAG, "getForecast Respose: $forecast")
        val properties = forecast.properties
//        Log.d(TAG, "getForecast Properties: $properties")
        val periods = properties.periods
        Log.d(TAG, "getForecast Periods: $periods")
        stateFlow.value = periods
    }
    init {
        viewModelScope.launch {
            try {
                val defaultLocation = locationRepository.getDefaultLocation()
                Log.d(TAG, "Default Location $defaultLocation")
                setForecastPeriods(defaultLocation)
                setHourlyForecast(defaultLocation)
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to fetch forecast", ex)
                apiError = true
                apiErrorMessage = ex.toString()
            }
        }
    }
    suspend fun addLocation(location: Location) {
        locationRepository.createLocation(location)
    }
    suspend fun listLocations(): List<Location> {
        return locationRepository.getLocations()
    }
}