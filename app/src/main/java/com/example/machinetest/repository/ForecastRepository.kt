package com.example.machinetest.repository


import ForecastResponse
import android.content.Context
import com.example.machinetest.db.AppDatabase
import com.example.machinetest.db.ForecastEntity
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForecastRepository(private val context: Context) {
    private val apiKey = "bbec6468b7df570a62d7ec3c4fed19a9" // <-- replace
    private val api = RetrofitClient.api
    private val dao = AppDatabase.getInstance(context).forecastDao()
    private val gson = Gson()

    suspend fun fetchForecastFromNetwork(location: String): ForecastResponse =
        withContext(Dispatchers.IO) {
            api.getFiveDayForecast(location, apiKey)
        }

    suspend fun saveForecastLocally(location: String, response: ForecastResponse) =
        withContext(Dispatchers.IO) {
            val json = gson.toJson(response)
            dao.upsert(ForecastEntity(location.lowercase(), json, System.currentTimeMillis()))
        }

    suspend fun getForecastFromLocal(location: String): ForecastResponse? =
        withContext(Dispatchers.IO) {
            val entity = dao.getForecastForLocation(location.lowercase())
            entity?.let {
                gson.fromJson(it.json, ForecastResponse::class.java)
            }
        }

    suspend fun getSavedLocations() = withContext(Dispatchers.IO) {
        dao.getAllSavedLocations()
    }
}