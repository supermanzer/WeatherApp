package com.supermanzer.weatherapp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.supermanzer.weatherapp.databinding.ForecastPeriodItemBinding
import org.json.JSONArray
import org.json.JSONObject


class ForecastViewHolder (
    private val binding: ForecastPeriodItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val TAG = "ForecastViewHolder"
    fun bind(forecastPeriod: Any) {
        val period = JSONObject(forecastPeriod.toString())
        val iconUrl = period["icon"]
        Log.d(TAG, "Forecast icon: $iconUrl")
        binding.forecastPeriodIcon.load(period["icon"])
        val titleString = "${period["name"]} - ${period["shortForecast"]}"
        binding.forecastPeriodName.text = titleString
        binding.forecastPeriodDesc.text = period["detailedForecast"].toString()
    }
}

class ForecastListAdapter(
    private val forecastItems: JSONArray
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
        return forecastItems.length()
    }
}