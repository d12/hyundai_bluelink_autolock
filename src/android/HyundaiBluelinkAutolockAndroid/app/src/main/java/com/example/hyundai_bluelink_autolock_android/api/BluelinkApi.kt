package com.example.hyundai_bluelink_autolock_android.api

interface BluelinkApi {
    suspend fun verifyCredentials(username: String, password: String, pin: String): Boolean
    suspend fun getApiKey(username: String, password: String): String
    suspend fun lockCar(apiKey: String, pin: String)
}