package com.supermanzer.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.supermanzer.weatherapp.databinding.FragmentLocationDetailBinding
import com.supermanzer.weatherapp.databinding.FragmentLocationListBinding
import com.supermanzer.weatherapp.db.Location
import java.util.UUID

class LocationDetailFragment: Fragment() {

    private lateinit var location: Location

    private var _binding: FragmentLocationDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is view visible?"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        location = Location(
            id = UUID.randomUUID(),
            name = "Pacific Grove, CA",
            lat = 36.622577,
            lon = -121.92643,
            forecastUrl = "https://api.weather.gov/gridpoints/MTR/93,54/forecast",
            forecastHourlyUrl = "https://api.weather.gov/gridpoints/MTR/93,54/forecast/hourly",
            isDefault = true
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            locationName.doOnTextChanged { text, _, _, _ ->
                location = location.copy(name = text.toString() )
            }
            locationName.apply {
                setText(location.name)
            }
            coordinates.apply {
                text = context.getString(
                    R.string.coordinates_string,
                    location.lat.toString(),
                    location.lon.toString()
                )
            }
            forecastUrl.apply {
                text = location.forecastUrl
            }
            forecastHourlyUrl.apply {
                text = location.forecastHourlyUrl
            }
            isDefault.apply {
                check(location.isDefault)
            }
            isDefault.setOnCheckedChangeListener { _, isChecked ->
                location = location.copy(isDefault = isChecked)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}