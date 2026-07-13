package com.supermanzer.weatherapp

sealed class Screen(val route: String) {
    object Layout : Screen("layout_screen")
    object NewLocation : Screen("new_location_screen")
    object LocationDetail : Screen("detail_screen")
    // Add other screens here
}