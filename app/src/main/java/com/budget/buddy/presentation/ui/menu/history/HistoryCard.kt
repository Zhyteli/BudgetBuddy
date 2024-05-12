@file:OptIn(ExperimentalMaterial3Api::class)

package com.budget.buddy.presentation.ui.menu.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.budget.buddy.R
import com.budget.buddy.presentation.ui.categories.CategoriesItemData
import com.budget.buddy.presentation.ui.categories.CategoryIcons
import com.budget.buddy.presentation.view.history.BarChart
import com.budget.buddy.presentation.view.history.PieChart

@Preview
@Composable
fun HistoryCard(
    isVisible: Boolean = true,
    onDismiss: () -> Unit = {},
    month: String = "January",
) {
    val animationDuration = 600 // Duration for the bottom sheet slide animation
    val overlayStartDelay =
        animationDuration / 2 // Start the overlay animation halfway through the bottom sheet animation
    var transaction by remember { mutableStateOf(false) }
    var categoryIcons by remember {
        mutableStateOf(
            CategoriesItemData(
                CategoryIcons.OTHER.title,
                CategoryIcons.OTHER.icons
            )
        )
    }
    val sumUser = remember { mutableStateOf("") }

    // State for controlling the overlay opacity
    val overlayAlpha by animateFloatAsState(
        targetValue = if (isVisible) 0.8f else 0f,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = overlayStartDelay // Delay before starting the alpha animation
        )
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = animationDuration)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = animationDuration)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Smoothly animated overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = overlayAlpha))
                    .clickable(
                        onClick = onDismiss,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )

            // Bottom Sheet Content
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Box(
                    Modifier
                        .background(Color(R.color.background))
                        .fillMaxSize()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = month,
                            fontFamily = FontFamily(Font(R.font.open)),
                            fontSize = 30.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.size(30.dp))
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            PieChart()
                        }
                        Spacer(modifier = Modifier.size(30.dp))
                        VertikalColumns()
                    }
                }
            }
        }
    }
}
@Composable
private fun VertikalColumns() {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Box(modifier = Modifier.background(Color(R.color.black))) {
            Box(modifier = Modifier.background(Color(R.color.black))){
                BarChart()
            }
        }
    }

}