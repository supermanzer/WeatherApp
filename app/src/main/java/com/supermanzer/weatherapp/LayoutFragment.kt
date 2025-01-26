package com.supermanzer.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.supermanzer.weatherapp.databinding.FragmentLayoutBinding
import com.supermanzer.weatherapp.db.Location


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val TAG = "LayoutFragment"


/**
 * A simple [Fragment] subclass.
 * Use the [LayoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LayoutFragment : Fragment() {
    private var _binding: FragmentLayoutBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private val forecastViewModel: ForecastViewModel by viewModels()

    private val currentConditionsFragment = CurrentConditionsFragment()
    private val hourlyForecastFragment = HourlyForecastFragment()
    private val dailyForecastFragment = DailyForecastFragment()


    private fun getLocationNames(locations: List<Location>): List<String> {
        return locations.map { it -> it.name }
    }

    private fun createSpinner(locations: List<Location>) {
        val spinner: Spinner = binding.locationSpinner
        spinner.onItemSelectedListener = object : AdapterView
        .OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                val location = locations[position]
                forecastViewModel.updateForecast(location)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        forecastViewModel.listLocations {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                getLocationNames(locations)
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            spinner.adapter = adapter

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLayoutBinding.inflate(inflater, container, false)

        forecastViewModel.locationList.observe(viewLifecycleOwner) { locations ->
            Log.d(TAG, "Location list received: $locations")
            createSpinner(locations)
            forecastViewModel.getDefaultLocationTitle { location ->
                if (location != null) {
                    val id = locations.indexOfFirst { it.name == location.name }
                    binding.locationSpinner.setSelection(id)
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Replace the FrameLayouts with the child fragments
        loadChildFragment(currentConditionsFragment, binding.currentConditions.id)
        loadChildFragment(hourlyForecastFragment, binding.hourlyForecast.id)
        loadChildFragment(dailyForecastFragment, binding.dailyForecast.id)


    }

    private fun loadChildFragment(fragment: Fragment, id: Int)  {
        childFragmentManager.beginTransaction()
            .replace(id, fragment)
            .commit()
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
                    LayoutFragmentDirections.layoutToLocation()
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}