package com.supermanzer.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.supermanzer.weatherapp.api.ForecastPeriod
import com.supermanzer.weatherapp.databinding.FragmentCurrentConditionsBinding
import kotlinx.coroutines.launch


private const val TAG = "CurrentConditionsFragment"


/**
 * A simple [Fragment] subclass.
 * Use the [CurrentConditionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CurrentConditionsFragment : Fragment() {
    private var _binding: FragmentCurrentConditionsBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private val viewModel: ForecastViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCurrentConditionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hourlyForecastPeriods.observe(viewLifecycleOwner) { items ->
                    Log.d(TAG, "Forecast period: $items[0]")
                    if (items.isNotEmpty()){
                        UpdateUi(items[0])
                    }
                }
            }
        }
    }

    private fun UpdateUi(forecastPeriod: ForecastPeriod) {
        binding.icon.load(forecastPeriod.icon)
        binding.summary.text = "Conditions: " + forecastPeriod.shortForecast
        val tempString = "${forecastPeriod.temperature.toInt()}° ${forecastPeriod.temperatureUnit}"
        binding.temp.text = tempString
        val windString = "Wind ${forecastPeriod.windSpeed} ${forecastPeriod.windDirection}"
        binding.wind.text = windString

    }
}