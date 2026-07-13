package com.supermanzer.weatherapp.compose

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.supermanzer.weatherapp.api.ForecastPeriod
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AltHourlyForecast(forecastItems: List<ForecastPeriod>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Hourly Forecast",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Use LazyRow for horizontal scrolling
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            // Display only the first 24 items
            items(forecastItems.take(24)) { item ->
                Log.d("AltHourlyForecast", "Processing item: $item")
                AltHourlyForecastItemView(item)
            }
        }
    }
}

@Composable
fun AltHourlyForecastItemView(item: ForecastPeriod) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Format the startTime to 12-hour format
            val formattedTime = formatTo12Hour(item.startTime)
            Text(
                text = formattedTime,
                fontSize = 16.sp
            )
            AsyncImage(
                model = item.icon,
                contentDescription = item.shortForecast,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "${item.temperature.toInt()}°${item.temperatureUnit}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
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