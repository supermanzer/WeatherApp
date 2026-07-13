import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import coil.compose.AsyncImage
import com.supermanzer.weatherapp.api.ForecastPeriod

@Composable
fun AltDailyForecast(forecastPeriodsLiveData: LiveData<List<ForecastPeriod>>) {
    val forecastPeriods by forecastPeriodsLiveData.observeAsState(initial = emptyList())

    DailyForecastContent(forecastPeriods = forecastPeriods)
}

@Composable
fun DailyForecastContent(forecastPeriods: List<ForecastPeriod>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
//            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Daily Forecast",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Use a regular Column instead of LazyColumn
        Column(
            modifier = Modifier.padding(PaddingValues(vertical = 8.dp))
        ) {
            forecastPeriods.forEach { item ->
                DailyForecastItemView(item)
            }
        }
    }
}

@Composable
fun DailyForecastItemView(item: ForecastPeriod) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
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
    }
}