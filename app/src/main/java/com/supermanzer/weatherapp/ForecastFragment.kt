package com.supermanzer.weatherapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.supermanzer.weatherapp.databinding.FragmentWeatherForecastBinding
import com.supermanzer.weatherapp.db.Location
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.util.UUID

private const val TAG = "ForecastFragment"

class ForecastFragment: Fragment() {
    private var _binding: FragmentWeatherForecastBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is view visible?"
        }
    private val forecastViewModel: ForecastViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Log.d(TAG, "onCreate running")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherForecastBinding.inflate(inflater, container, false)
        binding.forecastGrid.layoutManager = GridLayoutManager(context, 1)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreatded Running")
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                forecastViewModel.forecastPeriods.collect{ items ->
                    if (forecastViewModel.apiError) {
                        Log.d(TAG, "ForecastViewModel has api error")
                    } else {
                        Log.d(TAG, "Forecast periods received: $items")
                        binding.forecastGrid.adapter = ForecastListAdapter(items)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_location_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_location -> {
                Log.d(TAG, "New Location selected")
                showNewLocation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showNewLocation() {
        viewLifecycleOwner.lifecycleScope.launch {
            Log.d(TAG, "Creating new location")
            val newLocation = Location(
                id = UUID.randomUUID(),
                name = "",
                lat = null,
                lon = null,
                forecastUrl = null,
                forecastHourlyUrl = null,
                isDefault = true,
            )
            forecastViewModel.addLocation(newLocation)

        }
    }
}