package com.supermanzer.weatherapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CursorAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.supermanzer.weatherapp.databinding.FragmentWeatherForecastBinding
import com.supermanzer.weatherapp.db.Location
import com.supermanzer.weatherapp.db.LocationRepository
import kotlinx.coroutines.launch
import java.util.UUID

private const val TAG = "ForecastFragment"

class ForecastFragment: Fragment() {
    private var _binding: FragmentWeatherForecastBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is view visible?"
        }
    private val forecastViewModel: ForecastViewModel by viewModels()
//    private val locationRepository = LocationRepository.get()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Log.d(TAG, "onCreate running")
        Log.d(TAG,"This fragment has started!")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherForecastBinding.inflate(inflater, container, false)
        binding.forecastGrid.layoutManager = GridLayoutManager(context, 1)

//        val spinner: Spinner = binding.locationSpinner
//        spinner.onItemSelectedListener = object : AdapterView
//                .OnItemSelectedListener {
//                    override fun onItemSelected(parent: AdapterView<*>,
//                                                view: View, position: Int, id: Long) {
//                        val location = locationList!![position]
//                        forecastViewModel.updateForecastPeriods(location)
//                    }
//            override fun onNothingSelected(parent: AdapterView<*>) {}
//                }
//        forecastViewModel.listLocations {
//            locationList = it
//            val adapter = ArrayAdapter(
//                requireContext(),
//                android.R.layout.simple_spinner_item,
//                getLocationNames(it)
//            ).also { adapter ->
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            }
//            spinner.adapter = adapter
//
//        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        forecastViewModel.getDefaultLocationTitle { location ->
            if (location != null) {
//                (activity as AppCompatActivity).supportActionBar?.title = location.name
//                val id = locationList!!.indexOfFirst { it.name == location.name }
//                binding.locationSpinner.setSelection(id)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                forecastViewModel.forecastPeriods.observe(viewLifecycleOwner){ items ->
                    if (forecastViewModel.apiError) {
                        Log.d(TAG, "ForecastViewModel has api error")
                    } else {
                        Log.d(TAG, "Forecast periods received: $items")
                        binding.forecastGrid.adapter = ForecastListAdapter(items)
                    }
                }
                forecastViewModel.locationList.observe(viewLifecycleOwner) { locations ->
                    Log.d(TAG, "Location list received: $locations")

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
                Log.d(TAG, "Location menu item selected")
                findNavController().navigate(
                    ForecastFragmentDirections.toLocation()
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun getLocationNames(locations: List<Location>): List<String> {
        return locations.map { it -> it.name }
    }
}