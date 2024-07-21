package com.supermanzer.weatherapp.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.supermanzer.weatherapp.databinding.ListItemLocationBinding
import com.supermanzer.weatherapp.db.Location
import java.util.UUID


class LocationHolder (
    private val binding: ListItemLocationBinding,
    private val clickListener: ClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(location: Location, onLocationClicked: (id: UUID) -> Unit) {
        binding.locationItemName.text = location.name
        binding.locationItemIsDefaultValue.text = location.isDefault.toString()
        binding.deleteLocationButton.setOnClickListener{
            clickListener.onClick(location)
        }
        binding.root.setOnClickListener {
            onLocationClicked(location.id)
        }
    }
}
class LocationListAdapter(
    private val locations: List<Location>,
    private val clickListener: ClickListener,
    private val onLocationClicked: (id: UUID) -> Unit
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
        holder.bind(location, onLocationClicked)
    }
}