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

class BluelinkCanadaApi : BluelinkApi {

    private val client = OkHttpClient()
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

    override suspend fun lockCar(apiKey: String, pin: String) {
        // Implementation not yet provided
        TODO("Not yet implemented")
    }

    override suspend fun getApiKey(username: String, password: String): String {
        val url = "https://mybluelink.ca/tods/api/v2/login"
        val requestBody = """{"loginId": "$username", "password": "$password"}""".toRequestBody(jsonMediaType)
        val request = buildRequest(url, requestBody)

        return withContext(Dispatchers.IO) {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw BluelinkUnknownError("Unexpected code $response")

                val responseBody = response.body?.string() ?: throw BluelinkUnknownError("Response body is null")
                val loginResponse = parseResponse<BluelinkLoginResponse>(responseBody)

                loginResponse?.result?.token?.accessToken ?: handleLoginError(loginResponse)
            }
        }
    }

    override suspend fun verifyCredentials(username: String, password: String, pin: String): Boolean {
        val apiKey: String
        try {
            apiKey = getApiKey(username, password)
        } catch (e: Exception) {
            return when (e) {
                is BluelinkIncorrectCredentialsError, is BluelinkUnknownError -> false
                else -> throw e
            }
        }

        return try {
            val pAuth = verifyPin(apiKey, pin)
            pAuth != null
        } catch (e: Exception) {
            return when (e) {
                is BluelinkIncorrectCredentialsError, is BluelinkUnknownError -> false
                else -> throw e
            }
        }
    }

    private suspend fun verifyPin(apiKey: String, pin: String): String? {
        val verifyPinUrl = "https://mybluelink.ca/tods/api/vrfypin"
        val requestBody = """{"pin": "$pin"}""".toRequestBody(jsonMediaType)
        val request = buildRequest(verifyPinUrl, requestBody, apiKey)

        return withContext(Dispatchers.IO) {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null

                val responseBody = response.body?.string() ?: return@withContext null
                val verifyPinResponse = parseResponse<BluelinkVerifyPinResponse>(responseBody)

                if (verifyPinResponse?.responseHeader?.responseCode == 0) {
                    verifyPinResponse.result?.pAuth
                } else {
                    null
                }
            }
        }
    }

    private fun buildRequest(url: String, requestBody: okhttp3.RequestBody, apiKey: String? = null): Request {
        return Request.Builder()
            .url(url)
            .post(requestBody)
            .apply {
                addHeader("From", "CWP")
                addHeader("Language", "0")
                addHeader("offset", "-4")
                addHeader("content-type", "application/json;charset=UTF-8")
                apiKey?.let { addHeader("accesstoken", it) }
            }
            .build()
    }

    private inline fun <reified T> parseResponse(responseBody: String): T? {
        return try {
            val responseAdapter = moshi.adapter(T::class.java)
            responseAdapter.fromJson(responseBody)
        } catch (e: Exception) {
            throw BluelinkUnknownError("Failed to parse response: $responseBody")
        }
    }

    private fun handleLoginError(loginResponse: BluelinkLoginResponse?): Nothing {
        val errorDesc = loginResponse?.error?.errorDesc
        if (errorDesc?.contains("The login information you entered is incorrect") == true) {
            throw BluelinkIncorrectCredentialsError("Login failed: $errorDesc")
        } else {
            throw BluelinkUnknownError("Login failed: ${errorDesc ?: "Unknown error"}")
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
