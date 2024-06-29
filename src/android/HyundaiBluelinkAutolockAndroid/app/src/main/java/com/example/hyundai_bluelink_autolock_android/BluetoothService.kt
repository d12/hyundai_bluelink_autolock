package com.example.hyundai_bluelink_autolock_android

import android.app.Service
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class BluetoothService : Service() {

    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_ACL_CONNECTED == action) {
                Log.d("BluetoothReceiver", "Bluetooth device connected")
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED == action) {
                Log.d("BluetoothReceiver", "Bluetooth device disconnected")
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        // Register BluetoothReceiver dynamically
        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        }
        registerReceiver(bluetoothReceiver, filter)

        val notification = NotificationCompat.Builder(this, getString(R.string.foreground_service_notification_channel))
            .setContentTitle("Hyundai Autolock")
            .setContentText("Bluetooth service is running in the background.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("BluetoothService", "Service started")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("BluetoothService", "Service destroyed")

        unregisterReceiver(bluetoothReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
