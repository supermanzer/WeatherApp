package com.supermanzer.weatherapp.location

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.supermanzer.weatherapp.databinding.FragmentNewLocationBinding
import com.supermanzer.weatherapp.db.Location
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.reflect.typeOf

private const val TAG = "NewLocationFragment"
interface ClickListener {
    fun onClick(location: Location)
}
class NewLocationFragment: Fragment(), ClickListener {
    private val locationListViewModel: LocationListViewModel by viewModels()
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

        binding.locationLookup.setOnClickListener {
            lookupNewLocation()
        }
        binding.locationListRecycler.layoutManager = LinearLayoutManager(context)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "New Location"
        binding.locationPreview.isVisible = false
        binding.locationConfirm.isVisible = false
        binding.locationConfirm.isEnabled = false

        binding.locationConfirm.setOnClickListener {
           createLocation()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
               locationListViewModel.locations.collect { locations ->
                   binding.locationListRecycler.adapter = LocationListAdapter(
                       locations,
                       this@NewLocationFragment,
                       ::navigateToDetail
                   )
               }
                locationListViewModel.geocodeResult.collect { result ->
                    if (result != null) {
                     Log.d(TAG, "Location found: $result")
                        binding.locationPreview.isVisible = true
                        binding.locationPreview.text = result.formatted_address
                        binding.locationConfirm.isVisible = true
                        binding.locationConfirm.isEnabled = true
                    } else {
                        binding.locationPreview.isVisible = false
                        binding.locationConfirm.isVisible = false
                        binding.locationConfirm.isEnabled = false
                    }
                }
            }
        }

    }

    private fun createLocation() {
        if (locationListViewModel.geocodeResult.value != null) {
            locationListViewModel.addLocation(locationListViewModel.geocodeResult.value!!)
            binding.newLocationInput.setText("")
            binding.locationPreview.isVisible = false
            binding.locationConfirm.isVisible = false
            Toast.makeText(
                requireContext(),
                "Location added",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Log.d(TAG, "View model geocode result is null")
        }
    }
    private fun lookupNewLocation() {
        val locationName = binding.newLocationInput.text.toString()
        Log.d(TAG, "Look up location button clicked with value: $locationName")
        if (locationName.isNotBlank()) {
            locationListViewModel.lookupLocation(locationName)
        } else {
            Toast.makeText(
                requireContext(),
                "Please enter a location name",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
    override fun onClick(location: Location) {
        Log.d(TAG, "Location clicked: $location")
        locationListViewModel.deleteLocation(location)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun navigateToDetail(id: UUID) {
        findNavController().navigate(
            NewLocationFragmentDirections.toLocationDetail(id)
        )
    }
}