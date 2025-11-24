package com.example.currencyconverter1.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExchangeRateGraph(rate: Double, fromCode: String, toCode: String) {
    val lineColor = Brush.linearGradient(
        colors = listOf(Color(0xFF48E9CC), Color(0xFF04C8E0))
    )

    val points = remember {
        List(15) { index ->
            val base = 50f
            val randomFluctuation = (-15..15).random()
            base + randomFluctuation + index * 2
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "LIVE EXCHANGE RATE",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
            Text(
                text = "1 $fromCode = ${String.format("%.4f", rate)} $toCode",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)) {
            val path = Path()
            val spacing = size.width / (points.size - 1)
            path.moveTo(0f, size.height - points.first())

            for (i in 1 until points.size) {
                val prevX = (i - 1) * spacing
                val prevY = size.height - points[i - 1]
                val currentX = i * spacing
                val currentY = size.height - points[i]
                val controlX = (prevX + currentX) / 2
                path.quadraticBezierTo(controlX, prevY, currentX, currentY)
            }

            // Draw line graph
            drawPath(path = path, brush = lineColor, style = Stroke(width = 4f, cap = StrokeCap.Round))

            // Gradient fill below line
            val fillPath = Path().apply {
                addPath(path)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF48E9CC).copy(alpha = 0.25f), Color.Transparent)
                )
            )

            // Draw circle on last point
            val lastX = size.width
            val lastY = size.height - points.last()
            drawCircle(Color.White, radius = 8.dp.toPx(), center = Offset(lastX, lastY))
            drawCircle(Color(0xFF48E9CC), radius = 5.dp.toPx(), center = Offset(lastX, lastY))
        }
        GraphTimeSelectors()
    }
}

@Composable
fun GraphTimeSelectors() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text("24h", color = Color.White.copy(alpha = 0.7f))
        Text("12h", color = Color.White.copy(alpha = 0.7f))
        Text("6h", color = Color.White.copy(alpha = 0.7f))
        Text("1h", color = Color.White.copy(alpha = 0.7f))
        Text("Now", color = Color.White, fontWeight = FontWeight.Bold)
    }
}
