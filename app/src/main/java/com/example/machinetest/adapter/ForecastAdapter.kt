package com.example.weatherapp.ui

import ForecastItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.machinetest.R

class ForecastAdapter(
    private val items: MutableList<ForecastItem> = mutableListOf(),
    private val onClick: (ForecastItem) -> Unit
) : RecyclerView.Adapter<ForecastAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvTemp: TextView = view.findViewById(R.id.tvTemp)
        val tvDesc: TextView = view.findViewById(R.id.tvDesc)
        val ivIcon: ImageView = view.findViewById(R.id.ivIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_forecast, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]

        holder.tvDate.text = item.dtTxt
        holder.tvTemp.text = "${item.main.temp.toInt()}°C"
        holder.tvDesc.text = item.weather.firstOrNull()?.description ?: ""

        val icon = item.weather.firstOrNull()?.icon
        val url = "https://openweathermap.org/img/wn/${icon}@2x.png"
        Glide.with(holder.itemView).load(url).into(holder.ivIcon)

        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = items.size

    fun setAll(newItems: List<ForecastItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
