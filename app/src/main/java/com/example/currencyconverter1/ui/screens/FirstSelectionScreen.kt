package com.example.currencyconverter1.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter1.data.model.CountryWithRate
import com.example.currencyconverter1.util.getFlagResId
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.currencyconverter1.ui.viewmodel.CurrencyViewModel
import com.example.currencyconverter1.ui.viewmodel.FirstSelectionViewModel

@Composable
fun FirstSelectionScreen(
    vm: CurrencyViewModel,
    onNext: () -> Unit,
    firstSelectionViewModel: FirstSelectionViewModel = hiltViewModel()
) {
    val countries by firstSelectionViewModel.countries.collectAsState(initial = emptyList())
    val isLoading by firstSelectionViewModel.isLoading.collectAsState()

    var fromSelectedCode by remember { mutableStateOf<String?>(null) }
    var toSelectedCode by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) { firstSelectionViewModel.loadCountries() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "To Start\nConversion\nSelect",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 34.sp,
                style = LocalTextStyle.current.copy(
                    drawStyle = Stroke(width = 0.3f)
                )
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    Text("SELECT From Currency", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(countries, key = { "from_${it.country.ccode}" }) { country ->
                    val flagRes = getFlagResId(country.country.ccode)
                    CurrencyCard(
                        country = country,
                        isSelected = fromSelectedCode == country.country.ccode,
                        onClick = { fromSelectedCode = country.country.ccode },
                        flagId = flagRes
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("SELECT To Currency", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(countries, key = { "to_${it.country.ccode}" }) { country ->
                    val flagRes = getFlagResId(country.country.ccode)
                    CurrencyCard(
                        country = country,
                        isSelected = toSelectedCode == country.country.ccode,
                        onClick = { toSelectedCode = country.country.ccode },
                        flagId = flagRes
                    )
                }
            }

            // 🔹 Select Button
            Button(
                onClick = {
                    val from = countries.find { it.country.ccode == fromSelectedCode }
                    val to = countries.find { it.country.ccode == toSelectedCode }
                    if (from != null && to != null) {
                        vm.setCurrencies(from, to)
                        onNext()
                    }
                },
                enabled = fromSelectedCode != null && toSelectedCode != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text("Select", fontSize = 16.sp)
            }

        }
    }
}

@Composable
fun CurrencyCard(
    country: CountryWithRate,
    isSelected: Boolean,
    onClick: () -> Unit,
    flagId: Int
) {
    val bgColor = if (isSelected) Color(0xFF2D9B76) else Color.Transparent
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            // Flag ka size 24.dp
            Image(
                painter = painterResource(flagId),
                contentDescription = null,
                modifier = Modifier.size(24.dp) // Flag Size
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = buildAnnotatedString {
                    // 1. Country Code: White Color, Size 16.sp
                    withStyle(style = SpanStyle(color = Color.White, fontSize = 16.sp)) {
                        append(country.country.ccode) // e.g., "USD"
                    }

                    // 2. Space: White Color, Size 16.sp
                    withStyle(style = SpanStyle(color = Color.White, fontSize = 16.sp)) {
                        append(" ")
                    }

                    // 3. Country Name: Gray Color, Size 12.sp
                    withStyle(style = SpanStyle(color = Color.Gray, fontSize = 11.sp)) {
                        append(country.country.name) // e.g., "United States"
                    }
                }
            )
        }
    }
}
