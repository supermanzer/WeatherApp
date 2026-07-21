package com.supermanzer.weatherapp.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import coil.compose.AsyncImage
import com.supermanzer.weatherapp.R
import com.supermanzer.weatherapp.api.ForecastPeriod
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HourlyForecast(forecastItemsLiveData: LiveData<List<ForecastPeriod>>) {
    // Observe the LiveData and get the list of ForecastPeriod
    val forecastItems by forecastItemsLiveData.observeAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.hourly_forecast),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Use LazyRow for horizontal scrolling
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(forecastItems, key = { _, item -> item.number }) { index, item ->
                Row(
                    modifier = Modifier.height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (index > 0) {
                        VerticalDivider(modifier = Modifier.padding(horizontal = 12.dp))
                    }
                    HourlyForecastItemView(item)
                }
            }
        }
    }
}

@Composable
fun HourlyForecastItemView(item: ForecastPeriod) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Format the startTime to 12-hour format
        val formattedTime = formatTo12Hour(item.startTime)
        Text(
            text = formattedTime,
            style = MaterialTheme.typography.labelMedium
        )
        AsyncImage(
            model = item.icon,
            contentDescription = item.shortForecast,
            modifier = Modifier.size(46.dp)
        )
        Text(
            text = "${item.temperature.toInt()}°${item.temperatureUnit}",
            style = MaterialTheme.typography.titleMedium
        )
        PrecipitationChance(
            percent = item.probabilityOfPrecipitation?.value?.toInt() ?: 0,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// Helper function to format time to 12-hour format
fun formatTo12Hour(startTime: String): String {
    return try {
        val offsetDateTime = OffsetDateTime.parse(startTime)
        val formatter = DateTimeFormatter.ofPattern("h a", Locale.getDefault())
        offsetDateTime.format(formatter)
    } catch (e: Exception) {
        // Handle parsing errors, return original string or a default value
        startTime
    }
}
