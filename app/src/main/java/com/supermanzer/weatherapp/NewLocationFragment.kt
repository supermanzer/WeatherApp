package com.supermanzer.weatherapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.supermanzer.weatherapp.databinding.FragmentNewLocationBinding
import com.supermanzer.weatherapp.databinding.FragmentWeatherForecastBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val TAG = "NewLocationFragment"
class NewLocationFragment: Fragment() {
    private val locationListViewModel: LocationListViewModel by viewModels()
    private var job: Job? = null

    private var _binding: FragmentNewLocationBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewLocationBinding.inflate(inflater, container, false)

        binding.newLocationInput.doOnTextChanged { text, _, _, _ ->
            Log.d(TAG, "Location name entered $text")
        }
        binding.locationLookup.setOnClickListener {
            val newLocationText = binding.newLocationInput.text
            Log.d(TAG, "Look up location button clicked with value: $newLocationText")
            // TODO: Implement Geocoding API lookup if location provided, return error if not
        }
        binding.locationListRecycler.layoutManager = LinearLayoutManager(context)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Make asynchronous call to load the existing locations.
        viewLifecycleOwner.lifecycleScope.launch {
            // repeatOneLifecycle triggers this work when the fragment reaches a STARTED state and
            // continues as long as the fragment is visible.  It will cancel the work if the view is
            // destroyed and pause if the app is backgrounded
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val locations = locationListViewModel.loadLocations()
                Log.d(TAG, "Locations loaded: $locations")
                binding.locationListRecycler.adapter = LocationListAdapter(locations)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}