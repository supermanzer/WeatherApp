package com.supermanzer.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.supermanzer.weatherapp.databinding.FragmentCurrentConditionsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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
        // TODO: Implement shared view models

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCurrentConditionsBinding.inflate(inflater, container, false)
        return binding.root
    }


}