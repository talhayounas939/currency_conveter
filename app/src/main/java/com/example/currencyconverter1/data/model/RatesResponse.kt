package com.example.currencyconverter1.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatesResponse(
    @SerialName("success")
    val success: Boolean = true,

    @SerialName("base")
    val base: String = "",

    @SerialName("date")
    val date: String = "",

    @SerialName("rates")
    val rates: Map<String, Double> = emptyMap()
)