package com.supermanzer.weatherapp

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    var defaultLocation: Location? = null
    var apiError: Boolean = false
    var apiErrorMessage: String? = null

    private val _hourlyForecastPeriods: MutableStateFlow<List<ForecastPeriod>>  = MutableStateFlow(
        emptyList())
    private val _forecastPeriods: MutableLiveData<List<ForecastPeriod>> =
        MutableLiveData(emptyList())
    val forecastPeriods: LiveData<List<ForecastPeriod>>
        get() = _forecastPeriods
    val hourlyForecastPeriods: StateFlow<List<ForecastPeriod>>
        get() = _hourlyForecastPeriods.asStateFlow()



    fun updateForecastPeriods(location: Location) {
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
    private suspend fun updateHourlyForecast(location: Location) {
        if (location.forecastHourlyUrl !== null) {
            val url = location.forecastHourlyUrl
//            fetchUrlSetState(url, _hourlyForecastPeriods)
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
        val periods = weatherRepository.getForecast(url).properties.periods
        stateFlow.value = periods
    }
    init {
        viewModelScope.launch {
            try {
                defaultLocation = locationRepository.getDefaultLocation()
                Log.d(TAG, "Default Location $defaultLocation")
                if (defaultLocation != null) {
                    updateForecastPeriods(defaultLocation!!)
                    updateHourlyForecast(defaultLocation!!)
                }
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
    fun listLocations(callback: (List<Location>) -> Unit) {
        viewModelScope.launch {
            val locationList = locationRepository.getLocations()
            callback(locationList)
        }
    }
}