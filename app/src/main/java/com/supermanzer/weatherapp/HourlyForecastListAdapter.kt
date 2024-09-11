package com.supermanzer.weatherapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.supermanzer.weatherapp.api.ForecastPeriod
import com.supermanzer.weatherapp.databinding.ForecastHourlyPeriodItemBinding

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


class HourlyForecastViewHolder (
    private val binding: ForecastHourlyPeriodItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    private val hourFormatter = DateTimeFormatter.ofPattern("h a")
    fun bind(forecastPeriod: ForecastPeriod) {
        binding.hourlyForecastIcon.load(forecastPeriod.icon)
        val tempString = "${forecastPeriod.temperature.toInt()}° ${forecastPeriod.temperatureUnit}"
        binding.hourlyForecastTemp.text = tempString
        val offsetDateTime = OffsetDateTime.parse(forecastPeriod.startTime, formatter)
        val hour = offsetDateTime.toLocalDateTime().format(hourFormatter)
        binding.hourlyForecastPeriodTime.text = hour
    }
}

class HourlyForecastListAdapter (
    private val forecastPeriods: List<ForecastPeriod>
) : RecyclerView.Adapter<HourlyForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ForecastHourlyPeriodItemBinding.inflate(inflater, parent, false)
        return HourlyForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
        val item = forecastPeriods[position]
        holder.bind(item)

        // TODO: Set click listener for item
    }

    override fun getItemCount(): Int {
        return forecastPeriods.size
    }
}


