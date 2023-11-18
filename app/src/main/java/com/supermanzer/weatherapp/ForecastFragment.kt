package com.supermanzer.weatherapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.supermanzer.weatherapp.databinding.FragmentWeatherForecastBinding
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

private const val TAG = "ForecastFragment"

class ForecastFragment: Fragment() {
    private var _binding: FragmentWeatherForecastBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is view visible?"
        }
    private val forecastViewModel: ForecastViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherForecastBinding.inflate(inflater, container, false)
        binding.forecastGrid.layoutManager = GridLayoutManager(context, 1)
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        viewLifecycleOwner.lifecycleScope.launch {
//            try {
//                val response = WeatherRepository().getForecast()
//                val responseObj = JSONObject(response.string())
//                val propertires = JSONObject(responseObj["properties"].toString())
//                val periods = JSONArray(propertires["periods"].toString())
//                Log.d(TAG, "Forecast Periods: $periods")
//            } catch (ex: Exception) {
//                Log.e(TAG, "Failed to fetch forecast", ex)
//            }
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                forecastViewModel.forecastPeriods.collect{ items ->
                    Log.d(TAG, "Forecast periods received: $items")
                    binding.forecastGrid.adapter = ForecastListAdapter(items)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}