package com.example.currencyconverter1.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverter1.R
import com.example.currencyconverter1.data.model.CountryWithRate
import com.example.currencyconverter1.ui.components.CurrencyDropdown
import com.example.currencyconverter1.ui.components.Keypad
import com.example.currencyconverter1.ui.components.RateChart
import com.example.currencyconverter1.ui.viewmodel.CurrencyViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun ConversionScreen(vm: CurrencyViewModel) {
    val from by vm.fromCurrency.collectAsState()
    val to by vm.toCurrency.collectAsState()
    val amount by vm.amount.collectAsState()
    val rate by vm.rate.collectAsState()
    val error by vm.error.collectAsState()
    val countries by vm.countries.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            vm.clearError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF351059), Color(0xFF2D0F48), Color(0xFF131138))
                )
            )
    ) {
        if (from == null || to == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Please select currencies", color = Color.White)
            }
            return@Box
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color.Transparent
        ) {
            ConversionUI(
                amount = amount,
                fromCurrency = from!!,
                toCurrency = to!!,
                rate = rate,
                countries = countries,
                onAmountChange = { key -> vm.updateAmount(key) },
                onFromCurrencyChange = { currency -> vm.setFromCurrency(currency) },
                onToCurrencyChange = { currency -> vm.setToCurrency(currency) },
                onSwapCurrencies = { vm.swapCurrencies() },
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Composable
fun ConversionUI(
    amount: String,
    fromCurrency: CountryWithRate,
    toCurrency: CountryWithRate,
    rate: Double,
    countries: List<CountryWithRate>,
    onAmountChange: (String) -> Unit,
    onFromCurrencyChange: (CountryWithRate) -> Unit,
    onToCurrencyChange: (CountryWithRate) -> Unit,
    onSwapCurrencies: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showFromDropdown by remember { mutableStateOf(false) }
    var showToDropdown by remember { mutableStateOf(false) }

    val rotation by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    val formatter = remember {
        SimpleDateFormat("MMM dd, hh:mm a 'UTC'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }
    var currentTime by remember { mutableStateOf(formatter.format(Date())) }
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = formatter.format(Date())
            delay(1000)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Currency Converter",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF48E9CC), Color.White)
                            ),
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.7f),
                                offset = Offset(6f, 6f),
                                blurRadius = 10f
                            )
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    LiveIndicator()
                }

                Text(
                    text = currentTime,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 13.sp
                )
            }
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(60.dp)
                    .graphicsLayer(rotationY = rotation)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        RateChart(rate = rate, fromCurrency = fromCurrency, toCurrency = toCurrency)

        Spacer(modifier = Modifier.height(16.dp))

        // Glass Box for currency rows
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.05f)) // Semi-transparent white
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CurrencyRow(fromCurrency, amount, isFrom = true) { showFromDropdown = true }

            IconButton(
                onClick = onSwapCurrencies,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))

            ) {
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Swap Currencies",
                    tint = Color.White
                )
            }

            val converted = if (amount.isNotEmpty()) {
                ((amount.toDoubleOrNull() ?: 0.0) * rate).let { String.format("%.4f", it) }
            } else "0.0000"

            CurrencyRow(toCurrency, converted, isFrom = false) { showToDropdown = true }
        }

        Spacer(modifier = Modifier.weight(1f))
        Keypad(onKeyPress = onAmountChange)

        if (showFromDropdown) CurrencyDropdown(countries, onDismiss = { showFromDropdown = false }) {
            onFromCurrencyChange(it)
            showFromDropdown = false
        }
        if (showToDropdown) CurrencyDropdown(countries, onDismiss = { showToDropdown = false }) {
            onToCurrencyChange(it)
            showToDropdown = false
        }
    }
}

@Composable
fun LiveIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "live_indicator_transition")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "live_indicator_alpha"
    )

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ShowChart,
            contentDescription = null,
            tint = Color(0xFF48E9CC),
            modifier = Modifier.size(16.dp)
        )
        Box(
            modifier = Modifier
                .size(8.dp)
                .graphicsLayer { this.alpha = alpha }
                .background(Color(0xFFE91E63), CircleShape) // A vibrant pink/red
        )
        Text(
            text = "Live",
            color = Color.White.copy(alpha = 0.9f),
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.5f),
                    offset = Offset(1f, 1f),
                    blurRadius = 2f
                )
            )
        )
    }
}

@Composable
fun CurrencyRow(country: CountryWithRate, displayAmount: String, isFrom: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
                Text(text = getFlagEmojiForCountry(country.country.alpha2), fontSize = 28.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = country.country.ccode,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Box(
            modifier = Modifier
                .weight(1.5f)
                .horizontalScroll(rememberScrollState(), reverseScrolling = true)
        ) {
            Text(
                text = if (displayAmount.isEmpty() && isFrom) "1" else displayAmount,
                color = if (isFrom) Color.White else Color(0xFF48E9CC),
                fontSize = if (isFrom) 32.sp else 28.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                softWrap = false,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }
}

private fun getFlagEmojiForCountry(countryCode: String): String {
    if (countryCode.length != 2) return "🏳️"
    val firstLetter = Character.codePointAt(countryCode.uppercase(), 0) - 'A'.code + 0x1F1E6
    val secondLetter = Character.codePointAt(countryCode.uppercase(), 1) - 'A'.code + 0x1F1E6
    return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
}
