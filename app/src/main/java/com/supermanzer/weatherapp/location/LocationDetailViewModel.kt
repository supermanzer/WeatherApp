package com.supermanzer.weatherapp.location

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.supermanzer.weatherapp.db.Location
import com.supermanzer.weatherapp.db.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

private const val TAG = "LocationDetailViewModel"

class LocationDetailViewModel(locationId: UUID): ViewModel() {
    private val locationRepository = LocationRepository.get()

    private val _location: MutableStateFlow<Location?> = MutableStateFlow(null)
    val location: StateFlow<Location?> = _location.asStateFlow()

    init {
        viewModelScope.launch {
            Log.d(TAG, "LocationDetailViewModel created")
            _location.value = locationRepository.getLocation(locationId)
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
}

class LocationDetailViewModelFactory(
    private val locationId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationDetailViewModel(locationId) as T
    }
}