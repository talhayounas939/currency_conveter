package com.example.currencyconverter1.data.repository

import android.content.Context
import android.util.Log
import com.example.currencyconverter1.data.model.Country
import com.example.currencyconverter1.data.model.CountryWithRate
import com.example.currencyconverter1.util.JsonReader
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RatesRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: ExchangeApi
) {
    private val TAG = "RatesRepository"

    private val localCountries: List<Country> by lazy {
        JsonReader.safeLoadCountries(context).map { it.country }
    }

    suspend fun getCountriesWithLiveRates(base: String = "USD"): List<CountryWithRate> = withContext(Dispatchers.IO) {
        try {
            val response = api.getLatestRates(base)
            if (response == null || !response.success) {
                Log.w(TAG, "API response failed, using fallback rates")
                return@withContext getFallbackRates()
            }

            val ratesMap = response.rates
            Log.d(TAG, "Received ${ratesMap.size} rates from API")

            localCountries.map { country ->
                val rate = ratesMap[country.ccode.uppercase()] ?: ratesMap[country.alpha2.uppercase()]
                if (rate != null) {
                    CountryWithRate(country, rate)
                } else {
                    val fallbackRate = (50 + (country.id % 100)) / 100.0 + 0.5
                    CountryWithRate(country, fallbackRate)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getCountriesWithLiveRates failed: ${e.message}", e)
            getFallbackRates()
        }
    }

    private fun getFallbackRates(): List<CountryWithRate> {
        return localCountries.map { country ->
            val pseudoRate = (50 + (country.id % 100)) / 100.0 + 0.5
            CountryWithRate(country, pseudoRate)
        }
    }

    fun convertAmount(amount: Double, from: CountryWithRate, to: CountryWithRate): Double {
        if (from.rate <= 0) return 0.0
        return (amount / from.rate) * to.rate
    }

    fun getRate(from: CountryWithRate, to: CountryWithRate): Double {
        if (from.rate <= 0) return 0.0
        return to.rate / from.rate
    }
}