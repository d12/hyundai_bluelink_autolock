package com.example.hyundai_bluelink_autolock_android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import android.view.ViewGroup
import com.example.hyundai_bluelink_autolock_android.databinding.FragmentIdentifyCarBluetoothBinding
import com.example.hyundai_bluelink_autolock_android.databinding.FragmentOnboardingIntroBinding

class FragmentIdentifyCarBluetooth : Fragment() {

    private var _binding: FragmentIdentifyCarBluetoothBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIdentifyCarBluetoothBinding.inflate(inflater, container, false)
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