package com.supermanzer.weatherapp.db

import android.content.Context
import android.util.Log
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import java.util.UUID

private const val DATABASE_NAME="location-database.db"
private const val TAG = "LocationRepository"
class LocationRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {
    private val database: LocationDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            LocationDatabase::class.java,
            DATABASE_NAME
        )
        .build()

    suspend fun getLocations(): List<Location> {
        // This is not returning any records despite 3 records in the DB. Figure this out
        val result = database.locationDao().getLocations()
        Log.d(TAG, "Location results returned: $result")
        return result
    }
    suspend fun getDefaultLocation(): Location? {
        return database.locationDao().getDefaultLocation()
    }
    suspend fun getLocation(id: UUID): Location = database.locationDao().getLocation(id)
    suspend fun createLocation(location: Location) {
       database.locationDao().createLocation(location)
    }

    fun updateLocation(location: Location){
        Log.d(TAG, "Updating location: $location")
        coroutineScope.launch {
            database.locationDao().updateLocation(location)
        }
    }
    suspend fun deleteLocation(id: UUID) = database.locationDao().deleteLocation(id)

    companion object {
        private var INSTANCE: LocationRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = LocationRepository(context)
            }
        }

        fun get(): LocationRepository {
            return INSTANCE?:
            throw IllegalStateException("LocationRepository must be initialized")
    }
    }
}