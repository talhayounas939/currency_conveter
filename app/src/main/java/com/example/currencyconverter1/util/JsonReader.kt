package com.example.currencyconverter1.util

import android.content.Context
import com.example.currencyconverter1.R
import com.example.currencyconverter1.data.model.Country
import com.example.currencyconverter1.data.model.CountryWithRate
import kotlinx.serialization.json.Json

object JsonReader {

     fun safeLoadCountries(context: Context): List<CountryWithRate> {
        return try {
           val inputStream = context.resources.openRawResource(R.raw.countries)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val countries = Json.decodeFromString<List<Country>>(jsonString)
            countries.map { CountryWithRate(it, 0.0) } // Rates will be filled by API
             } catch (e: Exception) {
             e.printStackTrace()
             emptyList()
             }
         }
}