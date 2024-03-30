package com.supermanzer.weatherapp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.supermanzer.weatherapp.databinding.ListItemLocationBinding
import com.supermanzer.weatherapp.db.Location
import com.supermanzer.weatherapp.db.LocationRepository


class LocationHolder (
    private val binding: ListItemLocationBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(location: Location) {
        binding.locationItemName.text = location.id.toString()
        binding.locationItemUrl.text = "Forecast URL goes here"
        binding.deleteLocationButton.setOnClickListener{

            Log.d("LocationHolder", "Delete clicked: ${location.id}")
            // TODO figure out how to call a suspend operation here
        }
    }
}
class LocationListAdapter(
    private val locations: List<Location>
): RecyclerView.Adapter<LocationHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemLocationBinding.inflate(inflater, parent, false)
        return LocationHolder(binding)
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onBindViewHolder(holder: LocationHolder, position: Int) {
        val location = locations[position]
        holder.bind(location)
    }
}