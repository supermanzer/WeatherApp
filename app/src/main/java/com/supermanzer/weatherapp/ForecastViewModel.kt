package com.supermanzer.weatherapp

import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.supermanzer.weatherapp.api.ForecastPeriod

import com.supermanzer.weatherapp.db.Location
import com.supermanzer.weatherapp.db.LocationRepository

import kotlinx.coroutines.launch


private const val TAG = "ForecastViewModel"

class ForecastViewModel: ViewModel() {
//    val defaultLocation: LiveData<Location?>
    private val weatherRepository = WeatherRepository()
    private val locationRepository = LocationRepository.get()
//    var defaultLocation: Location? = null

    // Private MutableLiveData for the default location
    private val _defaultLocation = MutableLiveData<Location?>()

    // Public LiveData for observing changes to the default location
    val defaultLocation: LiveData<Location?> = _defaultLocation

    var apiError: Boolean = false
    var apiErrorMessage: String? = null

    private val _hourlyForecastPeriods: MutableLiveData<List<ForecastPeriod>>  = MutableLiveData(
        emptyList())
    private val _forecastPeriods: MutableLiveData<List<ForecastPeriod>> =
        MutableLiveData(emptyList())
    private val _locationList: MutableLiveData<List<Location>> = MutableLiveData(emptyList())
    val forecastPeriods: LiveData<List<ForecastPeriod>>
        get() = _forecastPeriods
    val hourlyForecastPeriods: LiveData<List<ForecastPeriod>>
        get() = _hourlyForecastPeriods
    val locationList: LiveData<List<Location>>
        get() = _locationList

    val currentConditions: LiveData<ForecastPeriod?> = MediatorLiveData<ForecastPeriod?>().apply {
        addSource(hourlyForecastPeriods) { periods ->
            value = periods.firstOrNull()
        }
    }



    private fun updateForecastPeriods(location: Location) {
        if (location.forecastUrl !== null) {
            val url = location.forecastUrl
            viewModelScope.launch {
                fetchUrlSetState(url, _forecastPeriods)
            }
        } else {
            apiError = true
            apiErrorMessage = "Forecast URL not set for location ${location.name}"
        }
    }
    private fun updateHourlyForecast(location: Location) {
        if (location.forecastHourlyUrl !== null) {
            val url = location.forecastHourlyUrl
            viewModelScope.launch {
                fetchUrlSetState(url, _hourlyForecastPeriods)
            }
        } else {
            apiError = true
            apiErrorMessage = "Hourly forecast URL not set for location ${location.name}"
        }
    }

    fun getDefaultLocationTitle(callback: (Location?) -> Unit) {
        viewModelScope.launch {
            val location = locationRepository.getDefaultLocation()
            callback(location)
        }
    }
    private suspend fun fetchUrlSetState(url: String, stateFlow: MutableLiveData<List<ForecastPeriod>>){
        try {
            val periods = weatherRepository.getForecast(url).properties.periods
            Log.d(TAG, "Fetched ${periods.size} forecast periods from ${url}")
            stateFlow.value = periods
        } catch (ex: Exception) {
            Log.e(TAG, "Failing to fetch forecast periods", ex)
        }
    }
    fun updateForecast(location: Location) {
        updateForecastPeriods(location)
        updateHourlyForecast(location)
    }
    init {
        viewModelScope.launch {
            try {
                val defaultLocation = locationRepository.getDefaultLocation()
                _defaultLocation.value = defaultLocation
                Log.d(TAG, "Default Location $defaultLocation")
                if (defaultLocation != null) {
                   updateForecast(defaultLocation!!)
                }
                locationRepository.getLocations().collect { locations ->
                    _locationList.value = locations
                }
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to fetch forecast", ex)
                apiError = true
                apiErrorMessage = ex.toString()
            }
        }
    }
    fun listLocations(callback: (List<Location>) -> Unit) {
        viewModelScope.launch {
            locationRepository.getLocations().collect { locations ->
                callback(locations)
            }
        }
    }
}