package com.supermanzer.weatherapp.compose

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.supermanzer.weatherapp.db.Location

private const val TAG = "LocationMenu"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationMenu(
    locationList: List<Location>,
    defaultLocation: Location?,
    updateForecast: (Location) -> Unit,
    onNewLocationClicked: () -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf(defaultLocation) }
    Log.d(TAG, "Selected Location: ${selectedLocation?.name}")
    LaunchedEffect(key1 = defaultLocation) {
        if (defaultLocation != null) {
            selectedLocation = defaultLocation
        }
    }

    ExposedDropdownMenuBox(
    expanded = expanded.value,
    onExpandedChange = { expanded.value = !expanded.value },
    modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = selectedLocation?.name ?: "Select a location",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            locationList.forEach { location ->
                DropdownMenuItem(
                    text = { Text(text = location.name) },
                    onClick = {
                        selectedLocation = location
                        updateForecast(location)
                        expanded.value = false
                        Log.d(TAG, "Selected location: ${location.name}")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text(text = "Add New Location") },
                onClick = {
                    onNewLocationClicked()
                    expanded.value = false
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}