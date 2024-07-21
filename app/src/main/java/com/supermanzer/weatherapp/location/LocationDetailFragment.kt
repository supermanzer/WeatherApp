package com.supermanzer.weatherapp.location

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
import androidx.navigation.fragment.navArgs
import com.supermanzer.weatherapp.ForecastViewModel
import com.supermanzer.weatherapp.R
import com.supermanzer.weatherapp.databinding.FragmentLocationDetailBinding
import com.supermanzer.weatherapp.db.Location
import com.supermanzer.weatherapp.db.LocationRepository
import kotlinx.coroutines.launch
import java.util.UUID
private const val TAG = "LocationDetailFragment"
class LocationDetailFragment: Fragment() {
    private val args: LocationDetailFragmentArgs by navArgs()
    private var _binding: FragmentLocationDetailBinding? = null
    private val forecastVewModel: ForecastViewModel by viewModels()
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is view visible?"
        }

    private val locationDetailViewModel: LocationDetailViewModel by viewModels {
        LocationDetailViewModelFactory(args.ID)
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
        Log.d(TAG, "onViewCreated: called")
        // TODO: Review BNR chapter on CrimeDetail fragment
        binding.apply {
            locationName.doOnTextChanged { text, _, _, _ ->
                locationDetailViewModel.updateLocation { oldLocation ->
                    Log.d(TAG, "Updating location name: $text")
                    oldLocation.copy(name = text.toString())
                }
            }
            isDefault.setOnCheckedChangeListener { _, isChecked ->
                locationDetailViewModel.updateLocation {oldLocation ->
                    Log.d(TAG, "Updating isDefault: $isChecked")
                    oldLocation.copy(isDefault = isChecked)
                }
                if (isChecked) {
                    Log.d(TAG, "Updating forecast periods to new location")
                    forecastVewModel.updateForecastPeriods(
                        locationDetailViewModel.location.value!!
                    )
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                locationDetailViewModel.location.collect { location ->
                    location?.let { updateUi(it) }
                }
            }

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: called")
        _binding = null
    }

    private fun updateUi(location: Location) {
        binding.apply {
            if (locationName.text.toString() != location.name) {
                locationName.setText(location.name)
            }
            coordinates.text = getString(
                R.string.coordinates_string,
                location.lat.toString(),
                location.lon.toString()
            )
            forecastUrl.text = location.forecastUrl
            forecastHourlyUrl.text = location.forecastHourlyUrl
            isDefault.isChecked = location.isDefault
        }
    }
}