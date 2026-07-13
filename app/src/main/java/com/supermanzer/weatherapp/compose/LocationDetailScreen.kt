package com.supermanzer.weatherapp.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.supermanzer.weatherapp.db.Location
import com.supermanzer.weatherapp.location.LocationDetailViewModel
import java.util.UUID

@Composable
fun LocationDetailScreen(
    locationId: UUID,
    navController: NavController,
    viewModel: LocationDetailViewModel = viewModel(
        factory = LocationDetailViewModel.LocationDetailViewModelFactory(
            locationId = locationId,
            context = LocalContext.current
        )
    )
) {
    val location by viewModel.location.collectAsState()
    val locationError by viewModel.locationError.collectAsState()

    LaunchedEffect(key1 = locationId) {
        viewModel.getLocationById(locationId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (locationError != null) {
            Text(text = "Error loading location")
        }
        if (location != null) {
            Text(
                text = location!!.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            DetailRow(label = "Latitude", value = location!!.lat.toString())
            DetailRow(label = "Longitude", value = location!!.lon.toString())
            DetailRow(label = "Daily Forecast URL", value = location!!.forecastUrl.toString())
            DetailRow(label = "Hourly Forecast URL", value = location!!.forecastHourlyUrl.toString())
            DetailRow(label = "Default Location", value = location!!.isDefault.toString())
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Return to Location Screen")
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}