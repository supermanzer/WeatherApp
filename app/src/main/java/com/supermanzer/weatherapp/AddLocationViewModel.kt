package com.supermanzer.weatherapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermanzer.weatherapp.api.GeocodeResult
import com.supermanzer.weatherapp.db.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "AddLocationViewModel"
class AddLocationViewModel : ViewModel() {
    private val locationRepository = LocationRepository.get()
    private val weatherRepository = WeatherRepository()

    private val _count = MutableStateFlow(0)
    val count = _count.asStateFlow()

    private val _geocodeResult = MutableStateFlow<GeocodeResult?>(null)
    val geocodeResult = _geocodeResult.asStateFlow()

    fun incrementCount() {
        val locationName = "Monterey, CA"
        viewModelScope.launch {
            val response = weatherRepository.getGeocodeResponse(locationName)
            _geocodeResult.value = response.results[0]
            Log.d(TAG, "Geocode Result: ${geocodeResult.value}")
            _count.value += 1
        }
    }
}