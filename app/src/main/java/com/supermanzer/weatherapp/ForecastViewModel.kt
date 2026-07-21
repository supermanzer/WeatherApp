package com.supermanzer.weatherapp

import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope

import com.supermanzer.weatherapp.api.ForecastPeriod

import com.supermanzer.weatherapp.db.FORECAST_TYPE_DAILY
import com.supermanzer.weatherapp.db.FORECAST_TYPE_HOURLY
import com.supermanzer.weatherapp.db.Location
import com.supermanzer.weatherapp.db.LocationRepository

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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

    private val _locationList: MutableLiveData<List<Location>> = MutableLiveData(emptyList())
    val locationList: LiveData<List<Location>>
        get() = _locationList

    // Room is the source of truth: switching locations re-subscribes to that
    // location's cached forecast, so cached data shows immediately and updates
    // reactively whenever refreshForecast() writes fresh data into the cache.
    val forecastPeriods: LiveData<List<ForecastPeriod>> = _selectedLocation.switchMap { location ->
        location?.let {
            locationRepository.observeForecastCache(it.id.toString(), FORECAST_TYPE_DAILY)
                .map { cache -> cache?.periods ?: emptyList() }
                .asLiveData()
        } ?: MutableLiveData(emptyList())
    }

    val hourlyForecastPeriods: LiveData<List<ForecastPeriod>> = _selectedLocation.switchMap { location ->
        location?.let {
            locationRepository.observeForecastCache(it.id.toString(), FORECAST_TYPE_HOURLY)
                .map { cache -> cache?.periods ?: emptyList() }
                .asLiveData()
        } ?: MutableLiveData(emptyList())
    }

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _refreshError = MutableLiveData<String?>(null)
    val refreshError: LiveData<String?> = _refreshError

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



    fun getDefaultLocationTitle(callback: (Location?) -> Unit) {
        viewModelScope.launch {
            val location = locationRepository.getDefaultLocation()
            callback(location)
        }
    }

    private suspend fun refreshForecast(location: Location) {
        _isRefreshing.value = true
        _refreshError.value = null
        try {
            coroutineScope {
                launch {
                    val url = location.forecastUrl
                    if (url == null) {
                        apiError = true
                        apiErrorMessage = "Forecast URL not set for location ${location.name}"
                    } else {
                        val periods = weatherRepository.getForecast(url).properties.periods
                        locationRepository.cacheForecast(location.id.toString(), FORECAST_TYPE_DAILY, periods)
                    }
                }
                launch {
                    val url = location.forecastHourlyUrl
                    if (url == null) {
                        apiError = true
                        apiErrorMessage = "Hourly forecast URL not set for location ${location.name}"
                    } else {
                        val periods = weatherRepository.getForecast(url).properties.periods.take(HOURLY_PERIOD_LIMIT)
                        locationRepository.cacheForecast(location.id.toString(), FORECAST_TYPE_HOURLY, periods)
                    }
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Failed to refresh forecast", ex)
            _refreshError.value = "Couldn't refresh forecast"
        } finally {
            _isRefreshing.value = false
        }
    }

    fun updateForecast(location: Location) {
        _selectedLocation.value = location
        viewModelScope.launch { refreshForecast(location) }
    }

    fun refresh() {
        _selectedLocation.value?.let { location ->
            viewModelScope.launch { refreshForecast(location) }
        }
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