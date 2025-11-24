package com.example.currencyconverter1.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.currencyconverter1.R

val JameelNooriNastaleeq = FontFamily(
    Font(R.font.jameel_noori_nastaleeq, FontWeight.Normal)
)

// Add the 'friendly.ttf' font to your 'res/font' directory to use this font.
// val friendly = FontFamily(
//     Font(R.font.friendly, FontWeight.Normal)
// )

// Set of Material typography styles to start with
val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default, // friendly,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default, // friendly,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default, // friendly,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = JameelNooriNastaleeq,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = JameelNooriNastaleeq,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = JameelNooriNastaleeq,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default, // friendly,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
    )
)
