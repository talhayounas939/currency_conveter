package com.example.currencyconverter1.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import java.util.Locale

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun CurrencyConverter1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val currentLocale = Locale.getDefault()
    val isUrdu = remember(currentLocale) { currentLocale.language == "ur" }

    val typography = if (isUrdu) {
        Typography.copy(
            bodyLarge = TextStyle(
                fontFamily = JameelNooriNastaleeq,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp // Adjust size for Urdu
            ),
            bodyMedium = TextStyle(
                fontFamily = JameelNooriNastaleeq,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            ),
            bodySmall = TextStyle(
                fontFamily = JameelNooriNastaleeq,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        )
    } else {
        Typography
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides if (isUrdu) LayoutDirection.Rtl else LayoutDirection.Ltr
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = Shapes,
            content = content
        )
    }
}