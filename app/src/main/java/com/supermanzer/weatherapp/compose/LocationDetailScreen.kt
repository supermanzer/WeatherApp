package com.supermanzer.weatherapp.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.supermanzer.weatherapp.R
import com.supermanzer.weatherapp.location.LocationDetailViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(location?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (locationError != null) {
                Text(text = stringResource(R.string.error_loading_location))
            }
            location?.let { loc ->
                Spacer(modifier = Modifier.height(16.dp))
                DetailRow(label = "Latitude", value = loc.lat.toString())
                DetailRow(label = "Longitude", value = loc.lon.toString())
                DetailRow(label = "Daily Forecast URL", value = loc.forecastUrl.toString())
                DetailRow(label = "Hourly Forecast URL", value = loc.forecastHourlyUrl.toString())
                DetailRow(label = "Office (CWA)", value = loc.cwa.toString())
                DetailRow(label = "Default Location", value = loc.isDefault.toString())
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.labelLarge)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 16.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
