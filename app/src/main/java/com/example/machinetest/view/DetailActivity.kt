package com.example.machinetest.view


import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.machinetest.R
import com.example.machinetest.repository.ForecastRepository
import com.example.machinetest.util.NetworkUtils
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private val repo by lazy { ForecastRepository(applicationContext) }
    private val gson = Gson()

    // Views
    private lateinit var tvLocation: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvTemp: TextView
    private lateinit var tvFeels: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvDesc: TextView
    private lateinit var tvSavedInfo: TextView
    private lateinit var ivIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Initialize views
        tvLocation = findViewById(R.id.tvLocation)
        tvDate = findViewById(R.id.tvDate)
        tvTemp = findViewById(R.id.tvTemp)
        tvFeels = findViewById(R.id.tvFeels)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvDesc = findViewById(R.id.tvDesc)
        tvSavedInfo = findViewById(R.id.tvSavedInfo)
        ivIcon = findViewById(R.id.ivIcon)

        // Get intent data
        val location = intent.getStringExtra("location") ?: ""
        val dtTxt = intent.getStringExtra("dtTxt") ?: ""
        val temp = intent.getDoubleExtra("temp", Double.NaN)
        val feels = intent.getDoubleExtra("feels", Double.NaN)
        val humidity = intent.getIntExtra("humidity", -1)
        val desc = intent.getStringExtra("desc") ?: ""
        val icon = intent.getStringExtra("icon") ?: ""

        // Set view data
        tvLocation.text = location
        tvDate.text = dtTxt
        tvTemp.text = "${temp.toInt()}°C"
        tvFeels.text = "Feels like: ${feels}°C"
        tvHumidity.text = "Humidity: $humidity%"
        tvDesc.text = desc

        val url = "https://openweathermap.org/img/wn/${icon}@2x.png"
        Glide.with(this).load(url).into(ivIcon)

        // Online check and save forecast locally
        val online = NetworkUtils.isOnline(this)
        tvSavedInfo.text = if (online) "Online — saving latest forecast for $location" else "Offline — showing cached view (if any)"

        if (online && location.isNotBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val resp = repo.fetchForecastFromNetwork(location)
                    repo.saveForecastLocally(location, resp)
                    runOnUiThread { tvSavedInfo.text = "Saved latest forecast for $location" }
                } catch (e: Exception) {
                    runOnUiThread { tvSavedInfo.text = "Failed to save: ${e.message}" }
                }
            }
        }
    }
}
