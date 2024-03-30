package com.supermanzer.weatherapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermanzer.weatherapp.db.Location
import com.supermanzer.weatherapp.db.LocationRepository
import kotlinx.coroutines.launch
import java.util.UUID

private const val TAG = "LocationListViewModel"
class LocationListViewModel: ViewModel() {
    private val locationRepository = LocationRepository.get()

    val locations = mutableListOf<Location>()
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
    fun deleteLocation(id: UUID) {
        viewModelScope.launch {
            locationRepository.deleteLocation(id)
        }
    }
}