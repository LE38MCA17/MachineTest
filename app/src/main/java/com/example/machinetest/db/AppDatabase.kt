package com.example.machinetest.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ForecastEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "weather_db"
                ).build().also { INSTANCE = it }
            }
    }
}
