package com.supermanzer.weatherapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.supermanzer.weatherapp.databinding.FramgentDailyForecastListBinding


class DailyForecastListFragment : Fragment() {
    private var _binding: FramgentDailyForecastListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is view visible?"
        }
    private val viewModel: ForecastViewModel by viewModels({requireParentFragment()})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DailyForecastListFragment", "onCreate running")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FramgentDailyForecastListBinding.inflate(inflater, container, false)
        binding.dailyForecastGrid.layoutManager = GridLayoutManager(context, 1)
        return binding.root
    }
}