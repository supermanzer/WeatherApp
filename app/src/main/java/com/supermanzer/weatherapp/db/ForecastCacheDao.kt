package com.supermanzer.weatherapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastCacheDao {
    @Query("SELECT * FROM cached_forecast WHERE locationId = :locationId AND type = :type")
    fun observe(locationId: String, type: String): Flow<CachedForecastEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: CachedForecastEntity)
}
