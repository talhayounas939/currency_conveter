package com.example.currencyconverter1.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.currencyconverter1.R
import kotlinx.coroutines.delay

private enum class SplashState { Initial, Final }

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    var splashState by remember { mutableStateOf(SplashState.Initial) }
    val logoTransition = updateTransition(splashState, label = "Logo Transition")

    var showText by remember { mutableStateOf(false) }

    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp.dp

    // --- Logo Animations --- //
    val rotationYAnim by logoTransition.animateFloat(
        transitionSpec = { tween(durationMillis = 2000, easing = FastOutSlowInEasing) },
        label = "rotationY"
    ) { state -> if (state == SplashState.Initial) 0f else 360f }

    val scale by logoTransition.animateFloat(
        transitionSpec = {
            keyframes {
                durationMillis = 2000
                0.7f at 0
                1.9f at 1300 with FastOutSlowInEasing
                1.0f at 2000 with EaseOutExpo
            }
        },
        label = "scale"
    ) { state -> if (state == SplashState.Initial) 0.7f else 1.0f }

    val offsetY by logoTransition.animateDp(
        transitionSpec = { tween(durationMillis = 2000, easing = FastOutSlowInEasing) },
        label = "offsetY"
    ) { state -> if (state == SplashState.Initial) 0.dp else -screenHeight * 0.13f }

    // --- Text Animations (start after logo settles) --- //
    val textAnimationDuration = 500
    val textAlpha by animateFloatAsState(
        targetValue = if (showText) 1f else 0f,
        animationSpec = tween(durationMillis = textAnimationDuration)
    )
    val textTranslationY by animateFloatAsState(
        targetValue = if (showText) 0f else -40f,
        animationSpec = tween(durationMillis = textAnimationDuration)
    )
    val textRotationX by animateFloatAsState(
        targetValue = if (showText) 0f else 90f,
        animationSpec = tween(durationMillis = textAnimationDuration)
    )

    // --- Main Effect Trigger --- //
    LaunchedEffect(Unit) {
        splashState = SplashState.Final // Start logo animation
        delay(2000) // Wait for logo animation to finish
        showText = true // Start text animation
        delay(2200) // Hold screen for 3 more seconds (total 3s)
        onTimeout()
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
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.offset(y = offsetY),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        rotationY = rotationYAnim
                    }
            )

            Text(
                text = stringResource(id = R.string.currency_converter),
                style = MaterialTheme.typography.headlineMedium.copy(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFE1F5FE), Color(0xFFB3E5FC))
                    ),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .graphicsLayer {
                        alpha = textAlpha
                        translationY = textTranslationY
                        rotationX = textRotationX
                    }
            )
        }
    }
}