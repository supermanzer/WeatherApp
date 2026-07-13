package com.supermanzer.weatherapp.compose

import AltDailyForecast
import com.supermanzer.weatherapp.Screen
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.supermanzer.weatherapp.ForecastViewModel
import com.supermanzer.weatherapp.api.ForecastPeriod
import com.supermanzer.weatherapp.db.Location

private const val TAG = "LayoutScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutScreen(navController: NavController, forecastViewModel: ForecastViewModel = viewModel()) {
    val locationList by forecastViewModel.locationList.observeAsState(initial = emptyList())
    val defaultLocation by forecastViewModel.defaultLocation.observeAsState()





    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        LocationMenu(
            locationList = locationList,
            defaultLocation = defaultLocation,
            updateForecast = { location -> forecastViewModel.updateForecast(location) },
            onNewLocationClicked = {
                Log.d(TAG, "onNewLocationClicked")
                navController.navigate(Screen.NewLocation.route)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CurrentConditions(
            forecastPeriodLiveData = forecastViewModel.currentConditions,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Log.d(TAG, "Hourly forecast periods: ${forecastViewModel.hourlyForecastPeriods.value?.size}")
        HourlyForecast(forecastItemsLiveData = forecastViewModel.hourlyForecastPeriods)

        Spacer(modifier = Modifier.height(16.dp))

        DailyForecast(forecastPeriodsLiveData = forecastViewModel.forecastPeriods)
    }
}