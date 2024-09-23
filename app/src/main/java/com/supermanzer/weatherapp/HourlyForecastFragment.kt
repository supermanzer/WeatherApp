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
import androidx.recyclerview.widget.LinearLayoutManager
import com.supermanzer.weatherapp.databinding.FragmentHourlyForecastBinding
import kotlinx.coroutines.launch

private const val TAG = "HourlyForecastFragment"


class HourlyForecastFragment : Fragment() {
    private var _binding : FragmentHourlyForecastBinding? = null
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
        _binding = FragmentHourlyForecastBinding.inflate(inflater, container, false)

        binding.hourlyForecastList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hourlyForecastPeriods.observe(viewLifecycleOwner) { items ->
                    Log.d(TAG, "Hourly forecast periods: $items")
                    binding.hourlyForecastList.adapter = HourlyForecastListAdapter(items)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}