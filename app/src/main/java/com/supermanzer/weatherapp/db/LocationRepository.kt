package com.supermanzer.weatherapp.db

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.supermanzer.weatherapp.api.ForecastPeriod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import java.util.UUID

private const val DATABASE_NAME="location-database.db"
private const val TAG = "LocationRepository"

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE locations ADD COLUMN cwa TEXT")
    }
}

private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS cached_forecast (
                locationId TEXT NOT NULL,
                type TEXT NOT NULL,
                periods TEXT NOT NULL,
                fetchedAt INTEGER NOT NULL,
                PRIMARY KEY(locationId, type)
            )
            """.trimIndent()
        )
    }
}

class LocationRepository @OptIn(DelicateCoroutinesApi::class) internal constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {
    private val database: LocationDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            LocationDatabase::class.java,
            DATABASE_NAME
        )
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
        .build()

    fun getLocations(): Flow<List<Location>> {
        // This is not returning any records despite 3 records in the DB. Figure this out
        val result = database.locationDao().getLocations()
        Log.d(TAG, "getLocations: $result")
        return result
    }
    suspend fun getDefaultLocation(): Location? {
        return database.locationDao().getDefaultLocation()
    }
    suspend fun getLocation(id: UUID): Location = database.locationDao().getLocation(id)

    suspend fun createLocation(location: Location)  {
        Log.d(TAG, "Creating location: $location")
       database.locationDao().createLocation(location)
    }

    fun updateLocation(location: Location){
        Log.d(TAG, "Updating location: $location")
        coroutineScope.launch {
            database.locationDao().updateLocation(location)
        }
    }
    suspend fun deleteLocation(id: UUID) = database.locationDao().deleteLocation(id)

    suspend fun updateIsDefault(id: UUID, isDefault: Boolean) {
        database.locationDao().unsetDefault()
        if (isDefault) {
            database.locationDao().setDefault(id)
        }
    }

    fun observeForecastCache(locationId: String, type: String): Flow<CachedForecastEntity?> =
        database.forecastCacheDao().observe(locationId, type)

    suspend fun cacheForecast(locationId: String, type: String, periods: List<ForecastPeriod>) {
        database.forecastCacheDao().upsert(
            CachedForecastEntity(locationId, type, periods, System.currentTimeMillis())
        )
    }


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