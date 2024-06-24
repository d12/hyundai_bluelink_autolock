package com.example.hyundai_bluelink_autolock_android.api

//Authentication error, string message
class BluelinkIncorrectCredentialsError(message: String) : Exception(message)

//Unknown error
class BluelinkUnknownError(message: String) : Exception(message)