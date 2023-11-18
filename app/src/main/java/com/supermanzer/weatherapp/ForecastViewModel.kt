package com.supermanzer.weatherapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

private const val TAG = "ForecastViewModel"

class ForecastViewModel: ViewModel() {
    private val weatherRepository = WeatherRepository()

    private val _forecastPeriods: MutableStateFlow<JSONArray> =
        MutableStateFlow(JSONArray())
    val forecastPeriods: StateFlow<JSONArray>
        get() = _forecastPeriods.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val respObj = JSONObject(weatherRepository.getForecast().string())
                val properties = JSONObject(respObj["properties"].toString())
                val periods = JSONArray(properties["periods"].toString())
                _forecastPeriods.value = periods
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to fetch forecast", ex)
            }
        }
    }
}