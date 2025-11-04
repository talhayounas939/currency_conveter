package com.example.currencyconverter1.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverter1.R

@Composable
fun StartScreen(onStartClick: () -> Unit) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp.dp
    val screenWidth = config.screenWidthDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            painter = painterResource(R.drawable.logo1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "Tap to begin your\nConversion journey",
            fontSize = 20.sp,
            modifier = Modifier.offset(y = -screenHeight * 0.25f),
            style = LocalTextStyle.current.copy(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xD300EFC5), Color(0xFF91A2E7))
                )
            ),
            color = Color.White
        )

        var clicked by remember { mutableStateOf(false) }
        val scale by animateFloatAsState(
            targetValue = if (clicked) 1.2f else 1f,
            animationSpec = tween(300)
        )

        val infiniteTransition = rememberInfiniteTransition()
        val arrowOffset by infiniteTransition.animateFloat(
            initialValue = -6f,
            targetValue = 6f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        Card(
            modifier = Modifier
                .width(screenWidth * 0.7f)
                .height(screenHeight * 0.17f)
                .align(Alignment.Center)
                .offset(y = screenHeight * 0.35f),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0x40A19B9B))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { clicked = !clicked; onStartClick() },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1), contentColor = Color.White),
                    modifier = Modifier.size(screenWidth * 0.18f).scale(scale)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Next",
                        modifier = Modifier.offset(x = arrowOffset.dp)
                    )
                }

                Text("Start", fontSize = 25.sp, color = Color.White)
            }
        }

        Text(
            "Powered by CC Exchange - v1.00",
            fontSize = 11.sp,
            color = Color(0x9EF6F6F6),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 19.dp)
        )
    }
}
