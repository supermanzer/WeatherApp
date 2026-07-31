package com.supermanzer.weatherapp.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AfdProductSummary(
    val id: String,
    val issuanceTime: String
)

@JsonClass(generateAdapter = true)
data class AfdProductsResponse(
    @Json(name = "@graph") val graph: List<AfdProductSummary>
)

@JsonClass(generateAdapter = true)
data class AfdProductDetail(
    val productText: String
)
