package com.supermanzer.weatherapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.supermanzer.weatherapp.api.ForecastPeriod
import com.supermanzer.weatherapp.databinding.ForecastPeriodItemBinding



class ForecastViewHolder (
    private val binding: ForecastPeriodItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(forecastPeriod: ForecastPeriod) {

        val iconUrl = forecastPeriod.icon
        binding.forecastPeriodIcon.load(iconUrl)
        val titleString = "${forecastPeriod.name} - ${forecastPeriod.shortForecast}"

        binding.forecastPeriodName.text = titleString
        binding.forecastPeriodDesc.text = forecastPeriod.detailedForecast
    }
}

class ForecastListAdapter(
    private val forecastItems: List<ForecastPeriod>
) : RecyclerView.Adapter<ForecastViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ForecastPeriodItemBinding.inflate(inflater, parent, false)
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val item = forecastItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return forecastItems.size
    }
}