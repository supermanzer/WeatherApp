package com.supermanzer.weatherapp.location

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.supermanzer.weatherapp.db.Location
import com.supermanzer.weatherapp.db.LocationDatabase
import com.supermanzer.weatherapp.db.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

private const val TAG = "LocationDetailViewModel"

class LocationDetailViewModel(
    locationId: UUID,
    locationRepository: LocationRepository
): ViewModel() {
   val locationRepository = LocationRepository.get()

    private val _location: MutableStateFlow<Location?> = MutableStateFlow(null)
    val location: StateFlow<Location?> = _location.asStateFlow()

    private val _locationError = MutableStateFlow<Exception?>(null)
    val locationError = _locationError.asStateFlow()

    init {
        getLocationById(locationId)
    }

    fun getLocationById(id: UUID) {
        viewModelScope.launch {
            try {
                val retrievedLocation = locationRepository.getLocation(id)
                Log.d(TAG, "Retrieved location: $retrievedLocation")
                _location.value = retrievedLocation
            } catch (e: Exception) {
                _locationError.value = e
                Log.e(TAG, "Error loading location", e)
            }
        }
    }

    fun updateLocation(onUpdate: (Location) -> Location) {
        Log.d(TAG, "Updating location")
        _location.update { oldLocation ->
            oldLocation?.let { onUpdate(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "LocationDetailViewModel destroyed")
        location.value?.let { locationRepository.updateLocation(it) }
    }
    class LocationDetailViewModelFactory(
        private val locationId: UUID,
        private val context: Context
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LocationDetailViewModel::class.java)) {
                val locationRepository = LocationRepository(context)
                @Suppress("UNCHECKED_CAST")
                return LocationDetailViewModel(locationId, locationRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}



