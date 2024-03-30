package com.supermanzer.weatherapp.api

import com.squareup.moshi.JsonClass

//@JsonClass(generateAdapter = true)
data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>,
)
//@JsonClass(generateAdapter = true)
data class GeoLocation (
    val lat: Double,
    val lng: Double,
)
//@JsonClass(generateAdapter = true)
data class Geometry (
    val location: GeoLocation,
    val location_type: String,
)
//@JsonClass(generateAdapter = true)
data class GeocodeResult (
    val address_components: List<AddressComponent>,
    val formatted_address: String,
    val geometry: Geometry,
    val place_id: String,
)
//@JsonClass(generateAdapter = true)
data class GeocodeResponse (
    val results: List<GeocodeResult>,
    val status: String,
)