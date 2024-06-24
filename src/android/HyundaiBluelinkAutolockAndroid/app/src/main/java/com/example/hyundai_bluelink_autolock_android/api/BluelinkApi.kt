package com.example.hyundai_bluelink_autolock_android.api

interface BluelinkApi {
    fun verifyCredentials(username: String, password: String, pin: String): Boolean
    fun getApiKey(username: String, password: String): String
    fun lockCar(apiKey: String, pin: String)
}