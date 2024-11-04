package com.supermanzer.weatherapp.location

import android.util.Log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermanzer.weatherapp.WeatherRepository
import com.supermanzer.weatherapp.api.GeocodeResult
import com.supermanzer.weatherapp.db.Location
import com.supermanzer.weatherapp.db.LocationRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.util.UUID

private const val TAG = "LocationListViewModel"
class LocationListViewModel: ViewModel() {
    private val locationRepository = LocationRepository.get()
    private val weatherRepository = WeatherRepository()

    private val _geocodeResult = MutableStateFlow<GeocodeResult?>(null)
    val geocodeResult = _geocodeResult.asStateFlow()

    private val _locationID = MutableStateFlow<UUID?>(null)
    val locationID = _locationID.asStateFlow()

    private val _locations: MutableStateFlow<List<Location>> = MutableStateFlow(emptyList())
    val locations: StateFlow<List<Location>>
        get() = _locations.asStateFlow()

    init {
        viewModelScope.launch {
            locationRepository.getLocations().collect { locations ->
                _locations.value = locations
            }
        }
    }


    fun addLocation(location: GeocodeResult) {
        viewModelScope.launch {
            val endpoints = weatherRepository.fetchForecastEndpoints(
                location.geometry.location.lat,
                location.geometry.location.lng
            )
            val dbLocation = Location(
                id = UUID.randomUUID(),
                name = location.formatted_address,
                lat = location.geometry.location.lat,
                lon = location.geometry.location.lng,
                isDefault = false,
                forecastUrl = endpoints.properties.forecast,
                forecastHourlyUrl = endpoints.properties.forecastHourly
            )
            Log.d(TAG, "addLocation: $dbLocation")
            _locationID.value = dbLocation.id
            locationRepository.createLocation(dbLocation)
        }
    }

    fun deleteLocation(location: Location) {
        viewModelScope.launch {
            locationRepository.deleteLocation(location.id)
        }
    }

    fun lookupLocation(locationName: String) {
        viewModelScope.launch {
            val response = weatherRepository.getGeocodeResponse(locationName)
            val primaryResult = response.results[0]
           _geocodeResult.value = primaryResult
        }
    }


}