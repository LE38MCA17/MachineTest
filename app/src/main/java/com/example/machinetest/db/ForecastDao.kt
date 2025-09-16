package com.example.machinetest.db


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(forecastEntity: ForecastEntity)

    @Query("SELECT * FROM forecast_table WHERE location = :location")
    suspend fun getForecastForLocation(location: String): ForecastEntity?

    @Query("SELECT location, savedAt FROM forecast_table")
    suspend fun getAllSavedLocations(): List<LocationInfo> // small DTO
}

data class LocationInfo(
    val location: String,
    val savedAt: Long
)
