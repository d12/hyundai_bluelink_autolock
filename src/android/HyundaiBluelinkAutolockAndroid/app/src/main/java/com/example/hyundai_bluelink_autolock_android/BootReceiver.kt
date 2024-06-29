package com.example.hyundai_bluelink_autolock_android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Boot completed")
            val serviceIntent = Intent(context, BluetoothService::class.java)
            context.startForegroundService(serviceIntent)
        }
    }
}
