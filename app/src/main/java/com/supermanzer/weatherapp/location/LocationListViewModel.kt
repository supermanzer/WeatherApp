package com.supermanzer.weatherapp.location

import android.util.Log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermanzer.weatherapp.WeatherRepository
import com.supermanzer.weatherapp.api.GeocodeResult
import com.supermanzer.weatherapp.db.Location
import com.supermanzer.weatherapp.db.LocationRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

private const val TAG = "LocationListViewModel"
class LocationListViewModel: ViewModel() {
    private val locationRepository = LocationRepository.get()
    private val weatherRepository = WeatherRepository()

    private val _result = MutableStateFlow<GeocodeResult?>(null)
    val locationResult = _result.asStateFlow()

    private val _locationID = MutableStateFlow<UUID?>(null)
    val locationID = _locationID.asStateFlow()

    var locations = mutableListOf<Location>()

    init {
        viewModelScope.launch {
            locations += loadLocations()
        }
    }

    fun getDefault(): Location {
        return locations.filter { it.isDefault }[0]
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
            _locationID.value = dbLocation.id
            locationRepository.createLocation(dbLocation)
            locations.add(dbLocation)
        }
    }
    suspend fun loadLocations(): List<Location> {
        val result = locationRepository.getLocations()
        Log.d(TAG, "${result.size} locations loaded")
        return result
    }
    fun deleteLocation(location: Location) {
        viewModelScope.launch {
            locationRepository.deleteLocation(location.id)
            locations.remove(location)
        }
    }

    fun lookupLocation(locationName: String) {
        viewModelScope.launch {
            val response = weatherRepository.getGeocodeResponse(locationName)
            _result.value = response.results[0]
        }
    }
}