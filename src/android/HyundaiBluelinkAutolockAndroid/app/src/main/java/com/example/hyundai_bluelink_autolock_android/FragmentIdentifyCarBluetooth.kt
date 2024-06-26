package com.example.hyundai_bluelink_autolock_android

import BluetoothDeviceAdapter
import BluetoothDeviceInfo
import android.Manifest
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyundai_bluelink_autolock_android.databinding.FragmentIdentifyCarBluetoothBinding

class FragmentIdentifyCarBluetooth : Fragment() {

    private var _binding: FragmentIdentifyCarBluetoothBinding? = null
    private val binding get() = _binding!!

    private val devices = mutableListOf<BluetoothDeviceInfo>()
    private lateinit var adapter: BluetoothDeviceAdapter

    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "android.bluetooth.device.action.ACL_DISCONNECTED") {
                val device = intent.getParcelableExtra<android.bluetooth.BluetoothDevice>(android.bluetooth.BluetoothDevice.EXTRA_DEVICE)
                device?.let {
                    val deviceName = if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                        it.name ?: "Unknown"
                    } else {
                        "Permission not granted"
                    }
                    val deviceInfo = BluetoothDeviceInfo(deviceName, it.address)
                    devices.add(deviceInfo)
                    adapter.notifyItemInserted(devices.size - 1)
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
        context?.registerReceiver(bluetoothReceiver, IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED"))
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
            device.
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun deviceClicked(name: String, mac: String) {
        println("Device clicked: $name, $mac")

//        Persist the device MAC in storage
        val sharedPref = activity?.getSharedPreferences(getString(R.string.car_bluetooth_mac_key), Context.MODE_PRIVATE)
        with (sharedPref?.edit()) {
            this?.putString(getString(R.string.car_bluetooth_mac_key), mac)
            this?.apply()
        }

//        TODO: On to next fragment!
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
