package com.supermanzer.weatherapp.location

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.supermanzer.weatherapp.databinding.ListItemLocationBinding
import com.supermanzer.weatherapp.db.Location
import java.util.UUID

// TODO: Review this approach for onCLick activity
// https://stackoverflow.com/questions/63838818/handling-button-clicks-in-recyclerview-adapter-kotlin



class LocationHolder (
    private val binding: ListItemLocationBinding,
    private val clickListener: ClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(location: Location) {
        binding.locationItemName.text = location.id.toString()
        binding.locationItemUrl.text = "Forecast URL goes here"
        binding.deleteLocationButton.setOnClickListener{
            clickListener.onClick(location)
        }
    }
}
class LocationListAdapter(
    private val locations: List<Location>,
    private val clickListener: ClickListener
): RecyclerView.Adapter<LocationHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemLocationBinding.inflate(inflater, parent, false)
        return LocationHolder(binding, clickListener)
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onBindViewHolder(holder: LocationHolder, position: Int) {
        val location = locations[position]
        holder.bind(location)
    }
}