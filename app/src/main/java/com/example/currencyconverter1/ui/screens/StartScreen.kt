package com.example.currencyconverter1.ui.screens

import android.app.Activity
import android.content.Context
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverter1.R
import java.util.*

@Composable
fun StartScreen(onStartClick: () -> Unit) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp.dp
    val screenWidth = config.screenWidthDp.dp
    val context = LocalContext.current
    var languageCode by rememberSaveable { mutableStateOf("en") }

    fun setLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources = context.resources
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        (context as? Activity)?.recreate()
    }

    Box(
        modifier = Modifier
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
        Button(
            onClick = {
                languageCode = if (languageCode == "en") "ur" else "en"
                setLocale(context, languageCode)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(
                text = if (languageCode == "en") "ur" else "en",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(screenHeight * 0.2f))

            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .alpha(1.0f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.tap_to_begin_your_conversion_journey),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                style = LocalTextStyle.current.copy(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xD300EFC5), Color(0xFF91A2E7))
                    )
                ),
                color = Color.White
            )

            Spacer(modifier = Modifier.weight(1f))

            var clicked by remember { mutableStateOf(false) }
            val scale by animateFloatAsState(
                targetValue = if (clicked) 1.2f else 1f,
                animationSpec = tween(300),
            )

            val infiniteTransition = rememberInfiniteTransition()
            val arrowOffset by infiniteTransition.animateFloat(
                initialValue = -6f,
                targetValue = 6f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
            )

            Card(
                modifier = Modifier
                    .width(screenWidth * 0.7f)
                    .height(screenHeight * 0.17f),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x40A19B9B))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.SpaceAround,
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
                            contentDescription = "Start",
                            modifier = Modifier.offset(x = arrowOffset.dp)
                        )
                    }
                    Text(stringResource(id = R.string.start), fontSize = 25.sp, color = Color.White)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                stringResource(id = R.string.powered_by_cc_exchange),
                fontSize = 11.sp,
                color = Color(0x9EF6F6F6),
                modifier = Modifier.padding(bottom = 19.dp)
            )
        }
    }
}