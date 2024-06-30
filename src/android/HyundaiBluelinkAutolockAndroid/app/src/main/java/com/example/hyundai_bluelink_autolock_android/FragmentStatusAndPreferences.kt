package com.example.hyundai_bluelink_autolock_android

import CarConnectionViewModel
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hyundai_bluelink_autolock_android.databinding.FragmentOnboardingIntroBinding
import com.example.hyundai_bluelink_autolock_android.databinding.FragmentStatusAndPreferencesBinding

class FragmentStatusAndPreferences : Fragment() {

    private var _binding: FragmentStatusAndPreferencesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CarConnectionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatusAndPreferencesBinding.inflate(inflater, container, false)

        viewModel = CarConnectionViewModelSingleton.getInstance(requireContext())

        viewModel.state.observe(viewLifecycleOwner) { newState ->

            println("I GOT AN UPDATE!!")
            println(newState)

            when (newState) {
                CarConnectionState.CONNECTED -> {
                    _binding!!.textView2.text = getString(R.string.car_status_connected)
                }

                CarConnectionState.DISCONNECTED_WAITING_FOR_TIMEOUT -> {
                    _binding!!.textView2.text = getString(R.string.car_status_waiting_for_timeout)
                }

                CarConnectionState.NOT_CONNECTED -> {
                    _binding!!.textView2.text = getString(R.string.car_status_not_connected)
                }

                else -> {
                    _binding!!.textView2.text = getString(R.string.car_status_unknown)
                }
            }
        }

        _binding!!.textView2.text = getString(R.string.car_status_not_connected)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}