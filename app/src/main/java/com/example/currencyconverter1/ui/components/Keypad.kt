package com.example.currencyconverter1.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Keypad(onKeyPress: (String) -> Unit) {
    androidx.compose.runtime.CompositionLocalProvider(
        androidx.compose.ui.platform.LocalLayoutDirection provides LayoutDirection.Ltr
    ) {
        val backspaceKey = "backspace"
        val keys = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf(".", "0", backspaceKey)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp)
            ) {
                keys.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        row.forEach { key ->
                            KeypadButton(
                                key = key,
                                isFunctionKey = key == backspaceKey,
                                onClick = { onKeyPress(if (key == backspaceKey) "Backspace" else key) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun KeypadButton(
    key: String,
    isFunctionKey: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.9f else 1f, label = "")

    // Removed custom LocalIndication override — use default Button ripple
    Box(
        modifier = Modifier
            .size(72.dp)
            .scale(scale)
            .clip(ButtonDefaults.shape),
        contentAlignment = Alignment.Center
    ) {
        // Blurred background with border
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.White.copy(alpha = 0.1f), shape = ButtonDefaults.shape)
                .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)), shape = ButtonDefaults.shape)
                .blur(radius = 12.dp, edgeTreatment = BlurredEdgeTreatment.Rectangle)
        )

        Button(
            onClick = onClick,
            modifier = Modifier.matchParentSize(),
            shape = ButtonDefaults.shape,
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            interactionSource = interactionSource
        ) {
            if (isFunctionKey) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Backspace",
                    tint = Color.White
                )
            } else {
                Text(
                    text = key,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
