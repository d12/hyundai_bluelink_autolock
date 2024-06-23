package com.example.hyundai_bluelink_autolock_android

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.Manifest
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.hyundai_bluelink_autolock_android.databinding.FragmentOnboardingPermissionsPrefaceBinding

class FragmentOnboardingPermissionsPreface : Fragment() {

    private var _binding: FragmentOnboardingPermissionsPrefaceBinding? = null
    private val binding get() = _binding!!

    private val requestBluetoothPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allPermissionsGranted = permissions.all { it.value }
            if (allPermissionsGranted) {
                progressToNextFragment()
            } else {
                // Handle the permission denied case
                permissions.forEach { (permission, isGranted) ->
                    if (!isGranted) {
                        println("Permission denied: $permission")

//                        TODO: Error handling here.
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingPermissionsPrefaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.permissionsPrefaceGrant.setOnClickListener {
            requestBluetoothPermission()
        }
    }

    private fun requestBluetoothPermission() {
        val requiredPermissions = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
        )

        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestBluetoothPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            println("Permissions have already been granted!")
            progressToNextFragment()
        }
    }

    private fun progressToNextFragment() {
        findNavController().navigate(R.id.action_FragmentOnboardingPermissionsPreface_to_FragmentOnboardingGatherBluelinkCredentials)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
