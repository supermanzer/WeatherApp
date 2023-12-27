package com.supermanzer.weatherapp.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LocationViewModel: ViewModel() {
    private val locationRepository = LocationRepository.get()

//    val locations = locationRepository.getLocations()
    val locations = mutableListOf<Location>()
    init {
        // TODO: Stub some default locations to test UI

        viewModelScope.launch {
            locations += locationRepository.getLocations()
        }
    }
}