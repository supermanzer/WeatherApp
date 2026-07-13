package com.supermanzer.weatherapp.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import coil.compose.AsyncImage
import com.supermanzer.weatherapp.api.ForecastPeriod
import java.time.LocalDateTime

// Data class to represent a daily forecast item
data class DailyForecastItem(
    val name: String,
    val temperature: String,
    val weatherIcon: String, // Resource ID for the weather icon
    val description: String
)

@Composable
fun DailyForecast(forecastPeriodsLiveData: LiveData<List<ForecastPeriod>>) {
    val forecastPeriods by forecastPeriodsLiveData.observeAsState(initial = emptyList())

    DailyForecastContent(forecastPeriods = forecastPeriods)
}

@Composable
fun DailyForecastContent(forecastPeriods: List<ForecastPeriod>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Daily Forecast",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.padding(PaddingValues(vertical = 8.dp))
        ) {
            forecastPeriods.forEach{ item ->
                DailyForecastItemView(item)
            }
        }
    }
}

@Composable
fun DailyForecastItemView(item: ForecastPeriod) {
    var expanded = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .clickable { expanded.value = !expanded.value },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.name,
                modifier = Modifier.weight(1f),
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
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (expanded.value) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.detailedForecast,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DailyForecastItemViewPreview() {
    val sampleForecast = ForecastPeriod(
        name = "Today",
        number = 1,
        startTime = LocalDateTime.now().toString(),
        endTime = LocalDateTime.now().plusDays(1).toString(),
        isDaytime = true,
        temperature = 75.0,
        temperatureUnit = "F",
        windSpeed = "10 mph",
        windDirection = "NW",
        shortForecast = "Sunny",
        detailedForecast = "Clear skies. High near 75F. Winds NW at 10 mph.",
        icon = "https://api.weather.gov/icons/land/day/sunny?size=medium"
    )
    MaterialTheme {
        DailyForecastItemView(item = sampleForecast)
    }
}

@Preview(showBackground = true)
@Composable
fun DailyForecastContentPreview() {
    val sampleForecasts = listOf(
        ForecastPeriod(
            name = "Today",
            number = 1,
            startTime = LocalDateTime.now().toString(),
            endTime = LocalDateTime.now().plusDays(1).toString(),
            isDaytime = true,
            temperature = 75.0,
            temperatureUnit = "F",
            windSpeed = "10 mph",
            windDirection = "NW",
            shortForecast = "Sunny",
            detailedForecast = "Clear skies. High near 75F. Winds NW at 10 mph.",
            icon = "https://api.weather.gov/icons/land/day/sunny?size=medium"
        ),
        ForecastPeriod(
            name = "Tonight",
            number = 2 ,
            startTime = LocalDateTime.now().plusHours(12).toString(),
            endTime = LocalDateTime.now().plusDays(1).toString(),
            isDaytime = false,
            temperature = 60.0,
            temperatureUnit = "F",
            windSpeed = "5 mph",
            windDirection = "N",
            shortForecast = "Mostly Clear",
            detailedForecast = "Mostly clear skies. Low near 60F. Winds N at 5 mph.",
            icon = "https://api.weather.gov/icons/land/night/few?size=medium"
        ),
        ForecastPeriod(
            name = "Tomorrow",
            number = 3,
            startTime = LocalDateTime.now().plusDays(1).toString(),
            endTime = LocalDateTime.now().plusDays(2).toString(),
            isDaytime = true,
            temperature = 80.0,
            temperatureUnit = "F",
            windSpeed = "12 mph",
            windDirection = "SW",
            shortForecast = "Partly Cloudy",
            detailedForecast = "Partly cloudy. High near 80F. Winds SW at 12 mph.",
            icon = "https://api.weather.gov/icons/land/day/bkn?size=medium"
        )
    )
    MaterialTheme {
        DailyForecastContent(forecastPeriods = sampleForecasts)
    }
}