package com.supermanzer.weatherapp.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.supermanzer.weatherapp.R
import com.supermanzer.weatherapp.db.Location

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationMenu(
    locationList: List<Location>,
    selectedLocation: Location?,
    onLocationSelected: (Location) -> Unit,
    onNewLocationClicked: () -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
    expanded = expanded.value,
    onExpandedChange = { expanded.value = !expanded.value },
    modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = selectedLocation?.name ?: stringResource(R.string.select_location),
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
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
                        onLocationSelected(location)
                        expanded.value = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.add_new_location)) },
                onClick = {
                    onNewLocationClicked()
                    expanded.value = false
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}