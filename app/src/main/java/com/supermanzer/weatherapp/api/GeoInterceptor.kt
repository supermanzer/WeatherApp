package com.supermanzer.weatherapp.api

import com.supermanzer.weatherapp.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor

import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor


private val geoApiKey = BuildConfig.MAPS_API_KEY

val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BASIC
}

class NwsUserAgentInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest: Request = chain.request().newBuilder()
            .header("User-Agent", "WeatherApp (${BuildConfig.APPLICATION_ID}, ryan.manzer@gmail.com)")
            .build()
        return chain.proceed(newRequest)
    }
}

class GeoInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val newUrl: HttpUrl = originalRequest.url.newBuilder()
            .addQueryParameter("key", geoApiKey)
            .build()
        val newRequest: Request = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}