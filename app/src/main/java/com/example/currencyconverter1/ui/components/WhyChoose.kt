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
fun WhyChoose(modifier: Modifier = Modifier, onSkipClick: () -> Unit) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp.dp

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.wc),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Why Choose\nCurrency Converter",
                modifier = Modifier
                    .padding(16.dp)
                    .offset(y = screenHeight * 0.6f),
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Currency Converter helps international\n" +
                        "import and export businesses by helping\n" +
                        "them determine the selling and buying\n" +
                        "profits of different products",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.offset(y = screenHeight * 0.6f),
                style = TextStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF91A2E7), Color(0xD300EFC5))
                    )
                )
            )
        }
    }
}
