package com.supermanzer.weatherapp.location

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermanzer.weatherapp.WeatherRepository
import com.supermanzer.weatherapp.api.GeocodeResponse

import com.supermanzer.weatherapp.db.Location
import com.supermanzer.weatherapp.db.LocationRepository
import kotlinx.coroutines.launch
import java.util.UUID

private const val TAG = "LocationListViewModel"
class LocationListViewModel: ViewModel() {
    private val locationRepository = LocationRepository.get()
    private val weatheRepository = WeatherRepository()

    var locations = mutableListOf<Location>()
    // TODO: Replace below with locations from database. We can hardcode those locations in the DB during dev.
    init {
        viewModelScope.launch {
            locations += loadLocations()
        }
    }

    fun getDefault(): Location {
        return locations.filter { it.isDefault }[0]
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

    fun lookupLocation(locationName: String){
        viewModelScope.launch {
            val location = weatheRepository.getGeocodeResponse(locationName)
//            val location: GeocodeResponse = weatheRepository.getTestRequest()
            Log.d(TAG, "Location: $location")
//            locationRepository.createLocation(location)
//            locations.add(location)
        }
    }
}