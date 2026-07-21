package com.supermanzer.weatherapp.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import coil.compose.AsyncImage
import com.supermanzer.weatherapp.R
import com.supermanzer.weatherapp.api.ForecastPeriod

@Composable
fun CurrentConditions(
    forecastPeriodLiveData: LiveData<ForecastPeriod?>,
    modifier: Modifier = Modifier,
    onDiscussionClick: () -> Unit = {}
) {
    val forecastPeriod by forecastPeriodLiveData.observeAsState()

    forecastPeriod?.let { period ->
        CurrentConditionsContent(
            forecastPeriod = period,
            modifier = modifier,
            onDiscussionClick = onDiscussionClick
        )
    }
}

@Composable
fun CurrentConditionsContent(
    forecastPeriod: ForecastPeriod,
    modifier: Modifier = Modifier,
    onDiscussionClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = largeIconUrl(forecastPeriod.icon),
                contentDescription = forecastPeriod.shortForecast,
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(20.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = forecastPeriod.shortForecast,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${forecastPeriod.temperature.toInt()}°",
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = stringResource(
                        R.string.wind_label,
                        forecastPeriod.windSpeed,
                        forecastPeriod.windDirection
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    forecastPeriod.probabilityOfPrecipitation?.value?.let { pop ->
                        PrecipitationChance(
                            percent = pop.toInt(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    forecastPeriod.relativeHumidity?.value?.let { humidity ->
                        Text(
                            text = stringResource(R.string.humidity_label, humidity.toInt()),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                TextButton(
                    onClick = onDiscussionClick,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(text = stringResource(R.string.forecast_discussion_link))
                }
            }
        }
    }
}

// NWS icon URLs arrive with ?size=small (56px); the hero renders at 120dp,
// so request the largest raster the API offers.
private fun largeIconUrl(url: String): String =
    url.replace(Regex("size=\\w+"), "size=large")
