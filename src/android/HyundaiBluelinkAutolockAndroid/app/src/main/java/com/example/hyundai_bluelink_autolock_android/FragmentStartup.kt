package com.example.hyundai_bluelink_autolock_android

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class FragmentStartup : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Check if user has completed onboarding
        val sharedPreferences = requireActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE)

        val savedBluetoothCarId = sharedPreferences.getString(this.getString(R.string.car_bluetooth_mac_key), null)
        val savedBluelinkUsername = SecureStorage(requireContext()).fetchCredential(getString(R.string.bluelink_storage_key_email))

        val finishedOnboarding = savedBluetoothCarId != null && savedBluelinkUsername != null

        val navController = findNavController()

        if (finishedOnboarding) {
            navController.navigate(R.id.action_FragmentStartup_to_FragmentStatusAndPreferences)
        } else {
            navController.navigate(R.id.action_FragmentStartup_to_FragmentOnboardingIntro)
        }
    }
}
