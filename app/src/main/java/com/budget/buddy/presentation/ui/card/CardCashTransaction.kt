package com.budget.buddy.presentation.ui.card


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.budget.buddy.R
import com.budget.buddy.data.impl.MonthCalculationImpl

@Preview
@Composable
fun CardCashTransaction(
    monthlyBudget: Double = 0.0,
    spent: Double = 0.0,
    balance: Double = 0.0
) {
    val infiniteTransition = rememberInfiniteTransition()
    val translateAnim by infiniteTransition.animateFloat(
        initialValue = 300f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 5000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val pearlGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF1E1E1E),  // Dark Gray
            Color(0xFF3C3F41),  // Medium Gray
            Color(0xFF4F5356),  // Lighter Gray with a hint of blue
            Color(0xFF1E1E1E)   // Dark Gray again for a smooth transition
        ),
        start = Offset(0f, 0f),
        end = Offset(translateAnim, translateAnim)
    )


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Box(
            modifier = Modifier
                .background(pearlGradient)
        ) {
            val month = remember { mutableStateOf("February") }
            month.value = MonthCalculationImpl.getMonth()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = month.value,
                    fontSize = 30.sp,
                    color = Color.White,
                    fontFamily = FontFamily(
                        Font(R.font.open)
                    )
                )

                Spacer(modifier = Modifier.height(25.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Monthly budget", fontSize = 20.sp, color = Color.White,
                        fontFamily = FontFamily(
                            Font(R.font.open)
                        )
                    )
                    Text(
                        text = "$monthlyBudget", fontSize = 20.sp, color = Color.White,
                        fontFamily = FontFamily(
                            Font(R.font.open)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Spent", fontSize = 20.sp, color = Color.White,
                        fontFamily = FontFamily(
                            Font(R.font.open)
                        )
                    )
                    Text(
                        text = "$spent", fontSize = 20.sp, color = Color.White,
                        fontFamily = FontFamily(
                            Font(R.font.open)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Balance", fontSize = 32.sp, color = Color.White,
                        fontFamily = FontFamily(
                            Font(R.font.open)
                        )
                    )
                    Text(
                        text = "$balance", fontSize = 32.sp, color = Color.White,
                        fontFamily = FontFamily(
                            Font(R.font.open)
                        )
                    )
                }
            }
        }
    }
}