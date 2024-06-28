package com.example.hyundai_bluelink_autolock_android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import android.view.ViewGroup
import com.example.hyundai_bluelink_autolock_android.databinding.FragmentOnboardingCompleteBinding
import com.example.hyundai_bluelink_autolock_android.databinding.FragmentOnboardingIntroBinding

class FragmentOnboardingComplete : Fragment() {

    private var _binding: FragmentOnboardingCompleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingCompleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    //  When the onboarding_second_para textview is clicked, open https://github.com/d12/hyundai_bluelink_autolock in the browser
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.finishButton.setOnClickListener {
//            findNavController().navigate(R.id.action_FragmentOnboardingIntro_to_FragmentOnboardingPermissionsPreface)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}