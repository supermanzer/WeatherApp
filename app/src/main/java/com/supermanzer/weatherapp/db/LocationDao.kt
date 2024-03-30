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
    @Query("SELECT * FROM locations")
    suspend fun getLocations(): List<Location>

    @Query("SELECT * FROM locations WHERE isDefault")
    suspend fun getDefaultLocation(): Location

    @Query("SELECT * FROM locations WHERE id=(:id)")
    suspend fun getLocation(id: UUID): Location

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createLocation(location: Location)

    @Query("SELECT COUNT(*) FROM locations")
    suspend fun recordCount(): Int

    @Query("UPDATE locations SET isDefault = False")
    suspend fun unsetDefault()

    @Query("UPDATE locations SET isDefault =True WHERE id=(:id)")
    suspend fun setDefault(id: UUID)

    @Query("DELETE FROM locations WHERE id=(:id)")
    suspend fun deleteLocation(id: UUID)
}