package com.supermanzer.weatherapp.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.supermanzer.weatherapp.ForecastViewModel
import com.supermanzer.weatherapp.R
import com.supermanzer.weatherapp.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutScreen(navController: NavController, forecastViewModel: ForecastViewModel = viewModel()) {
    val locationList by forecastViewModel.locationList.observeAsState(initial = emptyList())
    val selectedLocation by forecastViewModel.selectedLocation.observeAsState()
    val forecastDiscussion by forecastViewModel.forecastDiscussion.observeAsState()
    val forecastDiscussionLoading by forecastViewModel.forecastDiscussionLoading.observeAsState(initial = false)
    val forecastDiscussionError by forecastViewModel.forecastDiscussionError.observeAsState()
    val isRefreshing by forecastViewModel.isRefreshing.observeAsState(initial = false)
    val refreshError by forecastViewModel.refreshError.observeAsState()
    var showDiscussionDialog by remember { mutableStateOf(false) }

    if (showDiscussionDialog) {
        ForecastDiscussionDialog(
            text = forecastDiscussion,
            isLoading = forecastDiscussionLoading,
            error = forecastDiscussionError,
            onDismiss = {
                showDiscussionDialog = false
                forecastViewModel.clearForecastDiscussion()
            }
        )
    }

    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text(text = stringResource(R.string.app_name)) }
//            )
//        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { forecastViewModel.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 4.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LocationMenu(
                    locationList = locationList,
                    selectedLocation = selectedLocation,
                    onLocationSelected = { location -> forecastViewModel.updateForecast(location) },
                    onNewLocationClicked = { navController.navigate(Screen.NewLocation.route) }
                )

                refreshError?.let { message ->
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }

                CurrentConditions(
                    forecastPeriodLiveData = forecastViewModel.currentConditions,
                    modifier = Modifier.fillMaxWidth(),
                    onDiscussionClick = {
                        showDiscussionDialog = true
                        forecastViewModel.fetchForecastDiscussion()
                    }
                )

                HourlyForecast(forecastItemsLiveData = forecastViewModel.hourlyForecastPeriods)

                DailyForecast(forecastPeriodsLiveData = forecastViewModel.forecastPeriods)
            }
        }
    }
}
