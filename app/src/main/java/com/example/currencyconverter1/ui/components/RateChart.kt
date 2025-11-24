package com.example.currencyconverter1.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverter1.data.model.CountryWithRate
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight

private enum class TimeRange(val label: String) {
    DAY("D"),
    WEEK("W"),
    MONTH("M"),
    YEAR("Y")
}

@Composable
fun RateChart(
    rate: Double,
    fromCurrency: CountryWithRate,
    toCurrency: CountryWithRate
) {
    // sample data points (kept similar to your later-power-of-two example)
    val points = listOf(
        1f, 2f, 4f, 8f, 16f, 32f, 64f, 128f, 256f, 512f, 1024f
    )

    var selectedTimeRange by remember { mutableStateOf(TimeRange.WEEK) }
    var canvasSize = remember { androidx.compose.runtime.mutableStateOf(androidx.compose.ui.unit.IntSize(1, 1)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .onSizeChanged { canvasSize.value = it }
            ) {
                val w = size.width
                val h = size.height

                if (points.isEmpty()) return@Canvas

                val minY = points.minOrNull() ?: 0f
                val maxY = points.maxOrNull() ?: 1f
                val range = (maxY - minY).let { if (it == 0f) 1f else it }

                val stepX = if (points.size > 1) w / (points.size - 1) else w
                val linePath = Path()
                val fillPath = Path()

                points.forEachIndexed { index, value ->
                    val x = index * stepX
                    val normalized = (value - minY) / range
                    val y = h - (normalized * h)

                    if (index == 0) {
                        linePath.moveTo(x, y)
                        fillPath.moveTo(x, y)
                    } else {
                        linePath.lineTo(x, y)
                        fillPath.lineTo(x, y)
                    }
                }

                // close the fill path to the bottom
                fillPath.lineTo(w, h)
                fillPath.lineTo(0f, h)
                fillPath.close()

                // gradient fill under curve
                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF48E9CC).copy(alpha = 0.35f),
                            Color(0xFF48E9CC).copy(alpha = 0.0f)
                        ),
                        startY = 0f,
                        endY = h
                    ),
                    style = Fill
                )

                // draw the line
                drawPath(
                    path = linePath,
                    color = Color(0xFF48E9CC),
                    style = Stroke(
                        width = 4f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                // draw point markers
                points.forEachIndexed { index, value ->
                    val x = index * stepX
                    val normalized = (value - minY) / range
                    val y = h - (normalized * h)
                    drawCircle(
                        color = Color.White,
                        radius = 6f,
                        center = Offset(x, y)
                    )
                    drawCircle(
                        color = Color(0xFF48E9CC),
                        radius = 3f,
                        center = Offset(x, y)
                    )
                }
            }

            // Overlaid rate card (kept exactly as before)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.1f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "1 ${fromCurrency.country.ccode} = ${String.format("%.4f", rate)} ${toCurrency.country.ccode}",
                    color = Color.White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TimeRange.entries.forEach { timeRange ->
                val isSelected = selectedTimeRange == timeRange
                Text(
                    text = timeRange.label,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.7f),
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { selectedTimeRange = timeRange }
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            else Color.Transparent
                        )
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}
