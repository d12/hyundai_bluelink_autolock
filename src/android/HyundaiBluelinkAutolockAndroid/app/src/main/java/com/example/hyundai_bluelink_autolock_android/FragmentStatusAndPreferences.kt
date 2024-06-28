package com.example.hyundai_bluelink_autolock_android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import android.view.ViewGroup
import com.example.hyundai_bluelink_autolock_android.databinding.FragmentOnboardingIntroBinding
import com.example.hyundai_bluelink_autolock_android.databinding.FragmentStatusAndPreferencesBinding

class FragmentStatusAndPreferences : Fragment() {

    private var _binding: FragmentStatusAndPreferencesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatusAndPreferencesBinding.inflate(inflater, container, false)
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