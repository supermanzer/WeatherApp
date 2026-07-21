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

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


private const val TAG = "ForecastViewModel"
private const val HOURLY_PERIOD_LIMIT = 24

class ForecastViewModel: ViewModel() {
//    val defaultLocation: LiveData<Location?>
    private val weatherRepository = WeatherRepository()
    private val locationRepository = LocationRepository.get()
//    var defaultLocation: Location? = null

    // The location whose forecast is currently displayed. Seeded from the
    // default location at startup, then updated on every menu selection.
    private val _selectedLocation = MutableLiveData<Location?>()
    val selectedLocation: LiveData<Location?> = _selectedLocation

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

    private val _forecastDiscussion = MutableLiveData<String?>(null)
    val forecastDiscussion: LiveData<String?> = _forecastDiscussion

    private val _forecastDiscussionLoading = MutableLiveData(false)
    val forecastDiscussionLoading: LiveData<Boolean> = _forecastDiscussionLoading

    private val _forecastDiscussionError = MutableLiveData<String?>(null)
    val forecastDiscussionError: LiveData<String?> = _forecastDiscussionError

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
                fetchUrlSetState(url, _hourlyForecastPeriods, limit = HOURLY_PERIOD_LIMIT)
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
    private suspend fun fetchUrlSetState(
        url: String,
        stateFlow: MutableLiveData<List<ForecastPeriod>>,
        limit: Int? = null
    ) {
        try {
            val periods = weatherRepository.getForecast(url).properties.periods
            Log.d(TAG, "Fetched ${periods.size} forecast periods from ${url}")
            stateFlow.value = if (limit != null) periods.take(limit) else periods
        } catch (ex: Exception) {
            Log.e(TAG, "Failing to fetch forecast periods", ex)
        }
    }
    fun updateForecast(location: Location) {
        _selectedLocation.value = location
        updateForecastPeriods(location)
        updateHourlyForecast(location)
    }
    fun fetchForecastDiscussion() {
        val location = _selectedLocation.value ?: return
        _forecastDiscussionLoading.value = true
        _forecastDiscussionError.value = null
        viewModelScope.launch {
            try {
                val cwa = location.cwa ?: resolveCwa(location)
                _forecastDiscussion.value = weatherRepository.getForecastDiscussion(cwa)
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to fetch forecast discussion", ex)
                _forecastDiscussionError.value = "Unable to load forecast discussion"
            } finally {
                _forecastDiscussionLoading.value = false
            }
        }
    }

    private suspend fun resolveCwa(location: Location): String {
        val lat = location.lat ?: throw IllegalStateException("Location ${location.name} has no coordinates")
        val lon = location.lon ?: throw IllegalStateException("Location ${location.name} has no coordinates")
        val cwa = weatherRepository.fetchForecastEndpoints(lat, lon).properties.cwa
        val updatedLocation = location.copy(cwa = cwa)
        locationRepository.updateLocation(updatedLocation)
        _selectedLocation.value = updatedLocation
        return cwa
    }

    fun clearForecastDiscussion() {
        _forecastDiscussion.value = null
        _forecastDiscussionError.value = null
    }
    init {
        viewModelScope.launch {
            locationRepository.getLocations().collect { locations ->
                _locationList.value = locations
            }
        }
        viewModelScope.launch {
            try {
                val startLocation = locationRepository.getDefaultLocation()
                    ?: locationRepository.getLocations().first().firstOrNull()
                if (startLocation != null) {
                    updateForecast(startLocation)
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