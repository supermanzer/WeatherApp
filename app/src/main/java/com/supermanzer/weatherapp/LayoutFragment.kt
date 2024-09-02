package com.supermanzer.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.supermanzer.weatherapp.databinding.FragmentLayoutBinding


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



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLayoutBinding.inflate(inflater, container, false)
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
}