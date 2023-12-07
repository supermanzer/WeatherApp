package com.supermanzer.weatherapp.db

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalStateException
import java.util.UUID

private const val DATABASE_NAME="location-database"

class LocationRepository private constructor(context: Context) {
    private val database: LocationDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            LocationDatabase::class.java,
            DATABASE_NAME
        ).createFromAsset(DATABASE_NAME)
        .build()

    fun getLocations(): Flow<List<Location>> = database.locationDao().getLocations()

    suspend fun getLocation(id: UUID): Location = database.locationDao().getLocation(id)

    suspend fun createLocation(location: Location) = database.locationDao().createLocation(location)

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