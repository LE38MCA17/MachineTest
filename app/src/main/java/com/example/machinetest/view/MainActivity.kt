package com.example.machinetest.view


import ForecastItem
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.machinetest.R
import com.example.machinetest.util.NetworkUtils
import com.example.machinetest.viewmodel.ForecastViewModel
import com.example.weatherapp.ui.ForecastAdapter



class MainActivity : AppCompatActivity() {

    private val vm: ForecastViewModel by viewModels()
    private lateinit var adapter: ForecastAdapter
    private var currentResponseLocation = ""
    private lateinit var etLocation: EditText
    private lateinit var btnSearch: ImageView
    private lateinit var rvForecasts: RecyclerView
    private lateinit var llSaved: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etLocation = findViewById(R.id.etLocation)
        btnSearch = findViewById(R.id.ivSearchIcon)
        rvForecasts = findViewById(R.id.rvForecasts)
        llSaved = findViewById(R.id.llSaved)

        adapter = ForecastAdapter(mutableListOf()) { item -> openDetail(item) }
        rvForecasts.layoutManager = LinearLayoutManager(this)
        rvForecasts.adapter = adapter

        // Search button click
        btnSearch.setOnClickListener {
            val loc = etLocation.text.toString().trim()
            if (loc.isNotEmpty()) {
                currentResponseLocation = loc
                vm.loadForecast(loc, NetworkUtils.isOnline(this))
            }
        }

        vm.forecast.observe(this) { result ->
            result.onSuccess { resp ->
                supportActionBar?.title = "${resp.city.name}, ${resp.city.country}"
                adapter.setAll(resp.list)
                vm.loadSavedLocations()

            }
            result.onFailure { ex ->
                adapter.setAll(emptyList())
                vm.loadSavedLocations()
                toast("Error: ${ex.message}")
            }
        }

        vm.savedLocations.observe(this) { list ->
            llSaved.removeAllViews()
            list.forEach { (loc, _) ->
                val b = Button(this).apply {
                    text = loc
                    setOnClickListener {
                        etLocation.setText(loc)
                        btnSearch.performClick()
                    }
                }
                llSaved.addView(
                    b,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        // Load saved locations initially
        vm.loadSavedLocations()
    }

    private fun openDetail(item: ForecastItem) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("location", currentResponseLocation)
            putExtra("dt", item.dt)
            putExtra("dtTxt", item.dtTxt)
            putExtra("temp", item.main.temp)
            putExtra("feels", item.main.feels_like)
            putExtra("humidity", item.main.humidity)
            putExtra("desc", item.weather.firstOrNull()?.description ?: "")
            putExtra("icon", item.weather.firstOrNull()?.icon ?: "")
        }
        startActivity(intent)
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}

