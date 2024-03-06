package com.budget.buddy.presentation.ui.card

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.budget.buddy.data.impl.MonthCalculationImpl

@Preview
@Composable
fun CardCashTransaction(
    monthlyBudget: Double = 0.0,
    spent: Double = 0.0,
    balance: Double = 0.0
) {
    val pearlGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF8E8C9), // Светлый тон перламутра
            Color(0xFFA7CAD9), // Синий
            Color(0xFFD8BFD8), // Светлый фиолетовый
            Color(0xFFF8E8C9)  // Светлый тон перламутра
        ),
        start = Offset(0f, 0f),
        end = Offset(200f, 200f) // Направление градиента
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
        val month = remember {
            mutableStateOf("February")
        }
        month.value = MonthCalculationImpl.getMonth()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = month.value, fontSize = 30.sp)

            Spacer(modifier = Modifier.height(25.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Monthly budget", fontSize = 20.sp)
                Text(text = "$monthlyBudget", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Spent", fontSize = 20.sp)
                Text(text = "$spent", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Balance", fontSize = 32.sp)
                Text(text = "$balance", fontSize = 32.sp)
            }
        }
    }
}
