package com.example.currencyconverter1.util

import java.text.SimpleDateFormat
import java.util.*

fun Double.format(digits: Int): String = "%.${digits}f".format(this)

fun currentUtcTime(): String {
    val sdf = SimpleDateFormat("dd MMM HH:mm 'UTC'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(Date())
}