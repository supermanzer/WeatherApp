package com.supermanzer.weatherapp.compose

import com.supermanzer.weatherapp.Screen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.supermanzer.weatherapp.ForecastViewModel
import com.supermanzer.weatherapp.location.LocationListViewModel
import java.util.UUID

class ComposeActivity : ComponentActivity() {
    private val viewModel: ForecastViewModel by viewModels()
    private val locationViewModel: LocationListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.Layout.route
            ) {
                composable(Screen.Layout.route) {
                    LayoutScreen(
                        forecastViewModel = viewModel,
                        navController = navController
                    )
                }
                composable(Screen.NewLocation.route) {
                    NewLocationScreen(
                        viewModel = locationViewModel,
                        navController = navController
                    )
                }
                composable(
                    route = Screen.LocationDetail.route + "/{locationId}",
                    arguments = listOf(navArgument("locationId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val locationIdString = backStackEntry.arguments?.getString("locationId")
                    val locationId = UUID.fromString(locationIdString)
                    LocationDetailScreen(locationId = locationId, navController = navController)
                }
            }
        }
    }
}