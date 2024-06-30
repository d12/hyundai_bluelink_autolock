package com.example.hyundai_bluelink_autolock_android

import androidx.lifecycle.ViewModel

class CarConnectionViewModel : ViewModel() {
    var state: CarConnectionState = CarConnectionState.NOT_CONNECTED
}
