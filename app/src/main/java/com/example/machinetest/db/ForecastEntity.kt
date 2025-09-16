package com.example.machinetest.db


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast_table")
data class ForecastEntity(
    @PrimaryKey val location: String, // lowercase location text used as key
    val json: String,
    val savedAt: Long // epoch ms
)
