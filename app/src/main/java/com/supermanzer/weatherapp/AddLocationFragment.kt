package com.supermanzer.weatherapp

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.supermanzer.weatherapp.databinding.FragmentAddLocationBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddLocationFragment : Fragment() {

    companion object {
        fun newInstance() = AddLocationFragment()
    }

    private var _binding : FragmentAddLocationBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val viewModel: AddLocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddLocationBinding.inflate(inflater, container, false)

        binding.trigger.setOnClickListener {
            viewModel.incrementCount()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.count.collect { count ->
                    binding.clickCount.text = "Clicked $count times"
                }
            }
        }

        return binding.root
    }
}