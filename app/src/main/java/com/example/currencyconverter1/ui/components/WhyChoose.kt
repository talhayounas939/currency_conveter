package com.example.currencyconverter1.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverter1.R

@Composable
fun WhyChoose(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF351059),
                        Color(0xFF2D0F48),
                        Color(0xFF131138)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.wc),
                contentDescription = "illustration",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(top = 46.dp),
                contentScale = ContentScale.Fit,
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(id = R.string.why_choose_currency_converter),
                modifier = Modifier.padding(horizontal = 26.dp),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.currency_converter_helps_international),
                modifier = Modifier.padding(horizontal = 24.dp),
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FeatureItem(icon = Icons.Default.TrendingUp, text = stringResource(id = R.string.real_time))
                FeatureItem(icon = Icons.Outlined.Shield, text = stringResource(id = R.string.secure))
                FeatureItem(icon = Icons.Default.FlashOn, text = stringResource(id = R.string.fast))
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun FeatureItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF48E9CC),
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = CircleShape
                )
                .padding(4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = text, color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
    }
}
