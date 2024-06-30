package com.example.hyundai_bluelink_autolock_android

import android.app.Service
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

class BluetoothService : Service(), ViewModelStoreOwner {

    private lateinit var viewModel: CarConnectionViewModel
    private val handler = Handler(Looper.getMainLooper())
    private var timeoutRunnable: Runnable? = null
    private lateinit var sharedPref: SharedPreferences

    override val viewModelStore: ViewModelStore = ViewModelStore()

    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
            val deviceAddress = device?.address

            val storedMacAddress = sharedPref.getString(getString(R.string.car_bluetooth_mac_key), null)

            if (deviceAddress == storedMacAddress) {
                if (BluetoothDevice.ACTION_ACL_CONNECTED == action) {
                    onCarConnected()
                } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED == action) {
                    onCarDisconnected()
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        viewModel = ViewModelProvider(this).get(CarConnectionViewModel::class.java)

        // Initialize SharedPreferences
        sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE)

        // Register BluetoothReceiver dynamically
        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        }
        registerReceiver(bluetoothReceiver, filter)

        val notification = NotificationCompat.Builder(this, getString(R.string.foreground_service_notification_channel))
            .setContentTitle("Hyundai Autolock")
            .setContentText("Bluetooth service is running in the background.")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Ensure you have this icon
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Set the priority
            .build()

        startForeground(1, notification)

        Log.d("BluetoothService", "Service started with initial state: ${viewModel.state}")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(bluetoothReceiver)
        timeoutRunnable?.let { handler.removeCallbacks(it) }
        Log.d("BluetoothService", "Service destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun onCarConnected() {
        transitionState(CarConnectionState.CONNECTED)
    }

    private fun onCarDisconnected() {
        transitionState(CarConnectionState.DISCONNECTED_WAITING_FOR_TIMEOUT)
        timeoutRunnable = Runnable {
            transitionState(CarConnectionState.NOT_CONNECTED)
        }
        handler.postDelayed(timeoutRunnable!!, 30000) // 30 seconds timeout
    }

    private fun transitionState(newState: CarConnectionState) {
        val oldState = viewModel.state
        viewModel.state = newState
        Log.d("BluetoothService", "State transitioned from $oldState to $newState")
        if (oldState == CarConnectionState.DISCONNECTED_WAITING_FOR_TIMEOUT && newState == CarConnectionState.CONNECTED) {
            timeoutRunnable?.let { handler.removeCallbacks(it) }
            Log.d("BluetoothService", "Timeout cancelled due to reconnection")
        }
    }
}
