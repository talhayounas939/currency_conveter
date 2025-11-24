package com.example.currencyconverter1.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun OnboardingFooter(
    currentPage: Int,
    totalPages: Int,
    onSkip: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isUrdu = Locale.getDefault().language == "ur"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isUrdu) Arrangement.Start else Arrangement.SpaceBetween
    ) {
        // Page Indicators
        Row(
            modifier = Modifier.height(48.dp), // Ensure consistent height
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(totalPages) { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == currentPage) 16.dp else 12.dp)
                        .padding(4.dp)
                        .background(
                            color = if (index == currentPage) Color(0xD300EFC5) else Color.White.copy(
                                alpha = 0.3f
                            ),
                            shape = CircleShape
                        )
                )
            }
        }

        // "Skip" button is only shown on the last page
        if (currentPage == totalPages - 1) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (isUrdu) Arrangement.End else Arrangement.End
            ) {
                TextButton(
                    onClick = onSkip,
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = com.example.currencyconverter1.R.string.get_started),
                        fontSize = 17.sp,
                        color = Color.White,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Finish",
                        tint = Color.White,
                        modifier = if (isUrdu) Modifier.scale(scaleX = -1f, scaleY = 1f) else Modifier
                    )
                }
            }
        }
    }
}
