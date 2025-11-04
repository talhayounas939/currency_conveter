package com.example.currencyconverter1.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverter1.data.model.CountryWithRate
import com.example.currencyconverter1.ui.components.Keypad
import com.example.currencyconverter1.ui.viewmodel.CurrencyViewModel
import com.example.currencyconverter1.util.currentUtcTime
import com.example.currencyconverter1.util.getFlagResId
import kotlinx.coroutines.delay

@Composable
fun ConversionScreen(vm: CurrencyViewModel) {
    val isLoadingRates by vm.isLoadingRates.collectAsState()
    val from by vm.fromCurrency.collectAsState()
    val to by vm.toCurrency.collectAsState()
    val amount by vm.amount.collectAsState()
    val rate by vm.rate.collectAsState()

    if (from == null || to == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F2027)),
            contentAlignment = Alignment.Center
        ) {
            Text("Please select currencies", color = Color.White)
        }
        return
    }

    ConversionUI(
        amount = amount,
        fromCurrency = from!!,
        toCurrency = to!!,
        rate = rate,
        onAmountChange = { key -> vm.updateAmount(key) },
        isLoadingRates = isLoadingRates
    )
}

@Composable
fun ConversionUI(
    amount: String,
    fromCurrency: CountryWithRate,
    toCurrency: CountryWithRate,
    rate: Double,
    onAmountChange: (String) -> Unit,
    isLoadingRates: Boolean
) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp.dp
    val currentTime = remember { mutableStateOf(currentUtcTime()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime.value = currentUtcTime()
            delay(60000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF1B526B), Color(0xFF171A8A)))
            )
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Current time and rate info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(currentTime.value, color = Color.LightGray, fontSize = 12.sp)

            if (isLoadingRates) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(screenHeight * 0.02f))

        // From Currency Row
        CurrencyRow(fromCurrency, amount, isFrom = true)

        Spacer(modifier = Modifier.height(16.dp))

        // Rate Line
        Text(
            text = "1 ${fromCurrency.country.ccode.uppercase()} ≈ ${
                String.format(
                    "%.4f",
                    rate
                )
            } ${toCurrency.country.ccode.uppercase()}",
            color = Color.LightGray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Converted Amount
        val converted = if (amount.isNotEmpty()) {
            val amountValue = amount.toDoubleOrNull() ?: 0.0
            val convertedValue = amountValue * rate
            String.format("%.2f", convertedValue)
        } else {
            "0.00"
        }

        CurrencyRow(toCurrency, converted, isFrom = false)

        Spacer(modifier = Modifier.height(32.dp))

        // Keypad
        Keypad(onKeyPress = onAmountChange)
    }
}

@Composable
fun CurrencyRow(country: CountryWithRate, displayAmount: String, isFrom: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val flagRes = getFlagResId(country.country.ccode)

        Image(
            painter = painterResource(id = flagRes),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "${country.country.ccode}: $displayAmount",
            color = if (isFrom) Color.White else Color(0xFF00E676),
            fontSize = if (isFrom) 28.sp else 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}