package com.supermanzer.weatherapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface LocationDao {
    @Query("SELECT * FROM location")
    fun getLocations(): Flow<List<Location>>

    @Query("SELECT * FROM location WHERE id=(:id)")
    suspend fun getLocation(id: UUID): Location

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createLocation(location: Location)

    @Delete
    suspend fun deleteLocation(id: UUID)
}