package com.supermanzer.weatherapp.location

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.reflect.typeOf

private const val TAG = "NewLocationFragment"
interface ClickListener {
    fun onClick(location: Location)
}
class NewLocationFragment: Fragment(), ClickListener {
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

        binding.locationLookup.setOnClickListener {
            val newLocationText = binding.newLocationInput.text
            Log.d(TAG, "Look up location button clicked with value: $newLocationText")
            locationListViewModel.lookupLocation(newLocationText.toString())
        }
        binding.locationListRecycler.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragment = this
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "New Location"
        binding.locationPreview.isVisible = false
        binding.locationConfirm.isVisible = false
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val locations = locationListViewModel.loadLocations()
                Log.d(TAG, "Locations loaded: $locations")
                binding.locationListRecycler.adapter = LocationListAdapter(
                    locations, fragment
                ) { id ->
                    navigateToDetail(id)
                }
                locationListViewModel.locationResult.collect { location ->
                    if (location != null) {
                        Log.d(TAG, "Location found: $location")
                        binding.locationPreview.isVisible = true
                        binding.locationPreview.text = location.formatted_address
                        binding.locationConfirm.isVisible = true
                        binding.locationConfirm.setOnClickListener {
                            locationListViewModel.addLocation(location)
                            binding.newLocationInput.setText("")
                            binding.locationPreview.isVisible = false
                            binding.locationConfirm.isVisible = false
                        }
                    }
                }
            }
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