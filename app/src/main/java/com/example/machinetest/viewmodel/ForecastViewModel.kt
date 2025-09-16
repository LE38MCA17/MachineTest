package com.example.machinetest.viewmodel


import ForecastResponse
import android.app.Application
import androidx.lifecycle.*
import com.example.machinetest.repository.ForecastRepository
import kotlinx.coroutines.launch

class ForecastViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = ForecastRepository(application.applicationContext)

    private val _forecast = MutableLiveData<Result<ForecastResponse>>()
    val forecast: LiveData<Result<ForecastResponse>> = _forecast

    private val _savedLocations = MutableLiveData<List<Pair<String, Long>>>()
    val savedLocations: LiveData<List<Pair<String, Long>>> = _savedLocations

    fun loadForecast(location: String, isOnline: Boolean) {
        viewModelScope.launch {
            try {
                if (isOnline) {
                    val response = repo.fetchForecastFromNetwork(location)
                    // Save latest for location
                    repo.saveForecastLocally(location, response)
                    _forecast.postValue(Result.success(response))
                } else {
                    val local = repo.getForecastFromLocal(location)
                    if (local != null) _forecast.postValue(Result.success(local))
                    else _forecast.postValue(Result.failure(Exception("No saved data for $location")))
                }
            } catch (e: Exception) {
                // try local fallback
                val local = repo.getForecastFromLocal(location)
                if (local != null) _forecast.postValue(Result.success(local))
                else _forecast.postValue(Result.failure(e))
            }
        }
    }

    fun loadSavedLocations() {
        viewModelScope.launch {
            val locs = repo.getSavedLocations().map { Pair(it.location, it.savedAt) }
            _savedLocations.postValue(locs)
        }
    }
}
