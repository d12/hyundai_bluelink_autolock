package com.example.hyundai_bluelink_autolock_android.api

object BluelinkApiFactory {
    fun getClient(country: BluelinkCountry): BluelinkApi {
        return when (country) {
            BluelinkCountry.CANADA -> BluelinkCanadaApi()
            // Add cases for other countries as needed
        }
    }
}