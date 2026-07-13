package com.supermanzer.weatherapp.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import coil.compose.AsyncImage
import com.supermanzer.weatherapp.api.ForecastPeriod

@Composable
fun CurrentConditions(
    forecastPeriodLiveData: LiveData<ForecastPeriod?>,
    modifier: Modifier = Modifier
) {
    val forecastPeriod by forecastPeriodLiveData.observeAsState()

    forecastPeriod?.let { period ->
        CurrentConditionsContent(forecastPeriod = period, modifier = modifier)
    }
}

@Composable
fun CurrentConditionsContent(
    forecastPeriod: ForecastPeriod,
    modifier: Modifier = Modifier
) {
    var showTooltip by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { showTooltip = !showTooltip },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Image on the left, taking full height
            AsyncImage(
                model = forecastPeriod.icon,
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .size(120.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.FillHeight
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Text on the right, aligned to the image's right border
            Column(
                modifier = Modifier
                    .weight(0.6f)
            ) {
                Text(
                    text = "Conditions: ${forecastPeriod.shortForecast}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${forecastPeriod.temperature.toInt()}° ${forecastPeriod.temperatureUnit}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Wind ${forecastPeriod.windSpeed} ${forecastPeriod.windDirection}",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}