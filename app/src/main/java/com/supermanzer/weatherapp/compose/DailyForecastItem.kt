package com.supermanzer.weatherapp.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import coil.compose.AsyncImage
import com.supermanzer.weatherapp.R
import com.supermanzer.weatherapp.api.ForecastPeriod
import java.time.LocalDateTime

@Composable
fun DailyForecast(forecastPeriodsLiveData: LiveData<List<ForecastPeriod>>) {
    val forecastPeriods by forecastPeriodsLiveData.observeAsState(initial = emptyList())

    DailyForecastContent(forecastPeriods = forecastPeriods)
}

@Composable
fun DailyForecastContent(forecastPeriods: List<ForecastPeriod>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.daily_forecast),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))

        Column {
            forecastPeriods.forEachIndexed { index, item ->
                if (index > 0) {
                    HorizontalDivider()
                }
                DailyForecastItemView(item)
            }
        }
    }
}

@Composable
fun DailyForecastItemView(item: ForecastPeriod) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Surface(
        onClick = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        color = Color.Transparent
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = item.icon,
                        contentDescription = item.shortForecast,
                        modifier = Modifier.size(46.dp)
                    )
                }
                Text(
                    text = "${item.temperature.toInt()}°${item.temperatureUnit}",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.titleMedium
                )
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    item.probabilityOfPrecipitation?.value?.let { pop ->
                        PrecipitationChance(
                            percent = pop.toInt(),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            if (expanded) {
                Text(
                    text = item.detailedForecast,
                    modifier = Modifier.padding(bottom = 12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
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
            number = 2,
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
