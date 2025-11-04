package com.example.currencyconverter1.data.repository

import android.util.Log
import com.example.currencyconverter1.data.model.RatesResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

// ✅ Safe and modern opt-in (replace the old one)
@OptIn(ExperimentalSerializationApi::class)
class ExchangeApi @Inject constructor() {

    private val TAG = "ExchangeApi"

    // ✅ Json configuration for flexible API responses
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
        allowSpecialFloatingPointValues = true
    }

    // ✅ Configure Ktor HTTP client
    private val client = HttpClient(OkHttp) {
        install(HttpTimeout) {
            requestTimeoutMillis = 15_000
            connectTimeoutMillis = 15_000
            socketTimeoutMillis = 15_000
        }
        install(ContentNegotiation) {
            json(json)
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d(TAG, message)
                }
            }
            level = LogLevel.HEADERS
        }
    }

    // ✅ Fetch all currency rates
    suspend fun getLatestRates(base: String = "USD"): RatesResponse? = withContext(Dispatchers.IO) {
        try {
            val url = "https://api.exchangerate.host/latest?base=${base.uppercase()}"
            Log.d(TAG, "Fetching rates from: $url")

            val response = client.get(url)
            val responseText = response.bodyAsText()

            // Decode JSON into RatesResponse
            json.decodeFromString<RatesResponse>(responseText)

        } catch (e: Exception) {
            Log.e(TAG, "API call failed: ${e.message}", e)
            null
        }
    }

    // ✅ Fetch rates for specific symbols
    suspend fun getSpecificRates(base: String, symbols: List<String>): RatesResponse? = withContext(Dispatchers.IO) {
        try {
            val symbolsQuery = symbols.joinToString(",")
            val url = "https://api.exchangerate.host/latest?base=${base.uppercase()}&symbols=${symbolsQuery.uppercase()}"
            Log.d(TAG, "Fetching specific rates from: $url")

            val response = client.get(url)
            val responseText = response.bodyAsText()

            // Decode JSON into RatesResponse
            json.decodeFromString<RatesResponse>(responseText)

        } catch (e: Exception) {
            Log.e(TAG, "Specific rates API call failed: ${e.message}", e)
            null
        }
    }
}