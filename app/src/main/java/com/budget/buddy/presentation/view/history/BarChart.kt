package com.budget.buddy.presentation.view.history

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.budget.buddy.R
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BarChart(
    data: Map<String, Double> = mapOf(
        "Sample-1" to 15000.0,
        "Sample-2" to 12000.0,
        "Sample-3" to 11000.0,
    ),
    chartBarWidth: Dp = 35.dp,
    animDuration: Int = 1000
) {
    val maxValue = data.values.maxOrNull() ?: 0  // Maximum value for scaling
    val animateHeights = remember {
        mutableStateListOf<Dp>().also { list ->
            data.forEach { list.add(0.dp) } // Initialize with 0.dp for animation
        }
    }
    val colors = listOf(Color(0xFFBB86FC), Color(0xFF4CAF50), Color(0xFFF44336))

    LaunchedEffect(key1 = "barChart") {
        data.values.forEachIndexed { index, value ->
            val targetHeight = ((value / maxValue.toInt()) * 300.dp) / 3 // Scale factor of 300dp
            animateHeights[index] = targetHeight
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        val numberFormat = NumberFormat.getInstance(Locale.GERMANY)
        numberFormat.maximumFractionDigits = 2
        data.entries.forEachIndexed { index, entry ->
            val height by animateDpAsState(
                targetValue = animateHeights[index],
                animationSpec = tween(durationMillis = animDuration, easing = LinearOutSlowInEasing)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val formattedTotalSpent = numberFormat.format(entry.value)
                Text(
                    text = formattedTotalSpent,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.open)),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Box(
                    modifier = Modifier
                        .width(chartBarWidth)
                        .height(height / 3)
                        .background(colors[index % colors.size])
                )
                Text(
                    text = entry.key,
                    fontSize = 16.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.open)),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

