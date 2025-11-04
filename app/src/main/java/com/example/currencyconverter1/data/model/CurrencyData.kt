package com.example.currencyconverter1.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val id: Int,
    val alpha2: String,
    val ccode: String,
    val name: String,
    val currency: String = ccode
)

@Serializable
data class CountryWithRate(
    val country: Country,
    val rate: Double
)