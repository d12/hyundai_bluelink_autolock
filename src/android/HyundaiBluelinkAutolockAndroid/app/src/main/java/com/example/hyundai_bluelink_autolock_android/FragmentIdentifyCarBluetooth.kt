package com.example.hyundai_bluelink_autolock_android

import BluetoothDeviceAdapter
import BluetoothDeviceInfo
import android.Manifest
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyundai_bluelink_autolock_android.databinding.FragmentIdentifyCarBluetoothBinding

class FragmentIdentifyCarBluetooth : Fragment() {

    private var _binding: FragmentIdentifyCarBluetoothBinding? = null
    private val binding get() = _binding!!

    private val devices = mutableListOf<BluetoothDeviceInfo>()
    private lateinit var adapter: BluetoothDeviceAdapter

    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            val device = intent?.getParcelableExtra<android.bluetooth.BluetoothDevice>(android.bluetooth.BluetoothDevice.EXTRA_DEVICE)
            device?.let {
                val deviceName = if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                    it.name ?: "Unknown"
                } else {
                    "Permission not granted"
                }
                val deviceInfo = BluetoothDeviceInfo(deviceName, it.address)

                when (action) {
                    "android.bluetooth.device.action.ACL_CONNECTED" -> {
                        Log.d("BluetoothConnection", "Device connected: ${deviceInfo.name} - ${deviceInfo.mac}")
                        if (devices.contains(deviceInfo)) return
                        devices.add(deviceInfo)
                        adapter.notifyItemInserted(devices.indexOf(deviceInfo))
                    }
                    "android.bluetooth.device.action.ACL_DISCONNECTED" -> {
                        Log.d("BluetoothDisconnection", "Device disconnected: ${deviceInfo.name} - ${deviceInfo.mac}")
                        if (devices.contains(deviceInfo)) return
                        devices.add(deviceInfo)
                        adapter.notifyItemInserted(devices.indexOf(deviceInfo))
                    }
                }
            }
        }
    }

    private val requestBluetoothPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("BluetoothPermission", "Bluetooth permission granted")
        } else {
            Log.d("BluetoothPermission", "Bluetooth permission denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filter = IntentFilter().apply {
            addAction("android.bluetooth.device.action.ACL_CONNECTED")
            addAction("android.bluetooth.device.action.ACL_DISCONNECTED")
        }
        context?.registerReceiver(bluetoothReceiver, filter)
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

        // Check for Bluetooth permission and request it if not granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            requestBluetoothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
        }

        // Set up RecyclerView
        adapter = BluetoothDeviceAdapter(devices) { device ->
            deviceClicked(device.name, device.mac)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun deviceClicked(name: String, mac: String) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Are you sure this is your car?\n\n$name")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                // Persist the device MAC in storage
                val sharedPref = activity?.getSharedPreferences(getString(R.string.car_bluetooth_mac_key), Context.MODE_PRIVATE)
                with(sharedPref?.edit()) {
                    this?.putString(getString(R.string.car_bluetooth_mac_key), mac)
                    this?.apply()
                }

                // TODO: On to next fragment!
                Log.d("DeviceConfirmation", "Device added: $name, $mac")

//                Use action_FragmentIdentifyCarBluetooth_to_FragmentOnboardingComplete
                findNavController().navigate(R.id.action_FragmentIdentifyCarBluetooth_to_FragmentOnboardingComplete)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Confirm Car Bluetooth")
        alert.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(bluetoothReceiver)
    }
}
