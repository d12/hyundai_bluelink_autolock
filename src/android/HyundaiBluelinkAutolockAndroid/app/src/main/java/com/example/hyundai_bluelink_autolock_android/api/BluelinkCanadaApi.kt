package com.example.hyundai_bluelink_autolock_android.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class BluelinkCanadaApi : BluelinkApi {

    private val client = OkHttpClient()

    override suspend fun lockCar(apiKey: String, pin: String) {
        // Implementation not yet provided
        TODO("Not yet implemented")
    }

    override suspend fun getApiKey(username: String, password: String): String {
        val url = "https://mybluelink.ca/tods/api/v2/login"
        val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = """{"loginId": "$username", "password": "$password"}""".toRequestBody(jsonMediaType)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("From", "CWP")
            .addHeader("Language", "0")
            .addHeader("offset", "-4")
            .addHeader("content-type", "application/json;charset=UTF-8")
            .build()

        return withContext(Dispatchers.IO) {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw BluelinkUnknownError("Unexpected code $response")

                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val responseAdapter = moshi.adapter(BluelinkLoginResponse::class.java)
                val responseBody = response.body?.string()

                if (responseBody != null) {
                    val loginResponse: BluelinkLoginResponse?
                    try {
                        loginResponse = responseAdapter.fromJson(responseBody)
                    } catch (e: Exception) {
                        throw BluelinkUnknownError("Failed to parse response: $responseBody")
                    }

                    if (loginResponse?.result != null) {
                        return@withContext loginResponse.result.token.accessToken
                    } else if(loginResponse?.error != null) {
                        if (loginResponse.error.errorDesc.contains("The login information you entered is incorrect")) {
                            throw BluelinkIncorrectCredentialsError("Login failed: ${loginResponse.error.errorDesc}")
                        } else {
                            throw BluelinkUnknownError("Login failed: ${loginResponse.error.errorDesc}")
                        }
                    } else {
                        throw BluelinkUnknownError("Invalid response from server: no result or error")
                    }

                } else {
                    throw BluelinkUnknownError("Invalid response from server: response body is null")
                }
            }
        }
    }

    override suspend fun verifyCredentials(username: String, password: String, pin: String): Boolean {
        val apiKey: String
        try {
            apiKey = getApiKey(username, password)
        } catch (e: Exception) {
            when (e) {
                is BluelinkIncorrectCredentialsError, is BluelinkUnknownError -> {
                    return false
                }
                else -> throw e
            }
        }

        return try {
            val pAuth = verifyPin(apiKey, pin)
            pAuth != null
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun verifyPin(apiKey: String, pin: String): String? {
        val verifyPinUrl = "https://mybluelink.ca/tods/api/vrfypin"
        val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = """{"pin": "$pin"}""".toRequestBody(jsonMediaType)

        val request = Request.Builder()
            .url(verifyPinUrl)
            .post(requestBody)
            .addHeader("Accesstoken", apiKey)
            .addHeader("From", "CWP")
            .addHeader("Language", "0")
            .addHeader("offset", "-4")
            .addHeader("content-type", "application/json;charset=UTF-8")
            .build()

        return withContext(Dispatchers.IO) {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null

                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val responseAdapter = moshi.adapter(BluelinkVerifyPinResponse::class.java)
                val responseBody = response.body?.string()

                if (responseBody != null) {
                    val verifyPinResponse: BluelinkVerifyPinResponse?
                    try {
                        verifyPinResponse = responseAdapter.fromJson(responseBody)
                    } catch (e: Exception) {
                        return@withContext null
                    }

                    if (verifyPinResponse?.responseHeader?.responseCode == 0) {
                        return@withContext verifyPinResponse.result?.pAuth
                    }
                }

                return@withContext null
            }
        }
    }

    @JsonClass(generateAdapter = true)
    data class BluelinkVerifyPinResponse(
        @Json(name = "responseHeader") val responseHeader: ResponseHeader,
        @Json(name = "result") val result: VerifyPinResult?
    )

    @JsonClass(generateAdapter = true)
    data class VerifyPinResult(
        @Json(name = "pAuth") val pAuth: String
    )

    @JsonClass(generateAdapter = true)
    data class BluelinkLoginResponse(
        @Json(name = "responseHeader") val responseHeader: ResponseHeader?,
        @Json(name = "result") val result: Result?,
        @Json(name = "error") val error: Error?
    )

    @JsonClass(generateAdapter = true)
    data class ResponseHeader(
        @Json(name = "responseCode") val responseCode: Int,
        @Json(name = "responseDesc") val responseDesc: String
    )

    @JsonClass(generateAdapter = true)
    data class Result(
        @Json(name = "token") val token: Token
    )

    @JsonClass(generateAdapter = true)
    data class Token(
        @Json(name = "accessToken") val accessToken: String,
    )

    @JsonClass(generateAdapter = true)
    data class Error(
        @Json(name = "errorCode") val errorCode: String,
        @Json(name = "errorDesc") val errorDesc: String
    )
}
