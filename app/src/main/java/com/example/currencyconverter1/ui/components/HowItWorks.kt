package com.example.currencyconverter1.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverter1.R

@Composable
fun HowItWorks(modifier: Modifier = Modifier, onSkipClick: () -> Unit) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp.dp

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.hlw),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(screenHeight * 0.52f))

            Text(
                text = "How It Works?",
                modifier = Modifier.padding(16.dp),
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "A currency convter uses exchange rates\n" +
                        "to show users how the values of\n" +
                        "two currencies are related.",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.offset(y = -screenHeight * 0.03f),
                style = TextStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xD300EFC5), Color(0xFF91A2E7))
                    ),
                    fontSize = 20.sp
                )
            )
        }
    }
}