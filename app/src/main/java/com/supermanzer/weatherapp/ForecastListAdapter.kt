package com.supermanzer.weatherapp

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.supermanzer.weatherapp.api.ForecastPeriod
import com.supermanzer.weatherapp.databinding.ForecastPeriodItemBinding


private const val TAG = "ForecastListAdapter"
class ForecastViewHolder (
    private val binding: ForecastPeriodItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(forecastPeriod: ForecastPeriod, isExpanded: Boolean) {
        // Extracting data from forecastPeriod
        val iconUrl = forecastPeriod.icon
        val titleString = forecastPeriod.name
        val summaryString = forecastPeriod.shortForecast
        val tempDesc = if (forecastPeriod.isDaytime) "Hi" else "Low"
        val backgroundColor = if (forecastPeriod.isDaytime) "white" else "#F2F2F2"
        val tempInt = "${forecastPeriod.temperature.toInt()}° ${forecastPeriod.temperatureUnit}"
        val tempString = "$tempDesc $tempInt"
        val detailString = forecastPeriod.detailedForecast
        val expandableContent = binding.forecastExpandableContent

        // Setting visibility of expandable content
        if (isExpanded) {
            expandableContent.visibility = View.VISIBLE
        // TODO: Implement expandable content reveal animation
        } else {
            expandableContent.visibility = View.GONE
            // TODO: Implement expandable content hide animation
        }

        // Setting data to views
        binding.forecastPeriodIcon.load(iconUrl)
        binding.forecastPeriodName.text = titleString
        binding.forecastSummary.text = summaryString
        binding.forecastPeriodTemp.text = tempString
        binding.forecastPeriodItem.setBackgroundColor(
            Color.parseColor(backgroundColor)
        )
        binding.detailedForecastText.text = detailString
    }
}

class ForecastListAdapter(
    private val forecastItems: List<ForecastPeriod>
) : RecyclerView.Adapter<ForecastViewHolder>() {
    private val isExpanded = BooleanArray(forecastItems.size) {false}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ForecastPeriodItemBinding.inflate(inflater, parent, false)
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val item = forecastItems[position]
        holder.bind(item, isExpanded[position])

        holder.itemView.setOnClickListener {
            isExpanded[position] = !isExpanded[position]
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return forecastItems.size
    }
}