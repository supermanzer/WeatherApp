package com.supermanzer.weatherapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.supermanzer.weatherapp.databinding.FragmentDailyForecastBinding
import kotlinx.coroutines.launch

private const val TAG = "DailyForecastFragment"

class CustomLayoutManager(context: Context?) : LinearLayoutManager(context) {
    override fun canScrollVertically(): Boolean {
        return true
    }
}

class DailyForecastFragment : Fragment() {
    private var _binding: FragmentDailyForecastBinding? = null
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
        _binding = FragmentDailyForecastBinding.inflate(inflater, container, false)


        binding.dailyForecastList.layoutManager = CustomLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.forecastPeriods.observe(viewLifecycleOwner) { items ->
                    binding.dailyForecastList.adapter = ForecastListAdapter(items)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}