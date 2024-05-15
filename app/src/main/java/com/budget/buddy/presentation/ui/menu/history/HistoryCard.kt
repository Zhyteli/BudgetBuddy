@file:OptIn(ExperimentalMaterial3Api::class)

package com.budget.buddy.presentation.ui.menu.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.budget.buddy.R
import com.budget.buddy.data.impl.MonthCalculationImpl
import com.budget.buddy.data.impl.work.SumAllCategories
import com.budget.buddy.domain.items.SpendingItem
import com.budget.buddy.domain.mapper.MapperColorsToItem
import com.budget.buddy.domain.mapper.convert.ConvertTime
import com.budget.buddy.presentation.ui.categories.CategoriesItemData
import com.budget.buddy.presentation.ui.categories.CategoryIcons
import com.budget.buddy.presentation.ui.list.categories.CategoriesList
import com.budget.buddy.presentation.view.history.BarChart
import com.budget.buddy.presentation.view.history.PieChart
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Preview
@Composable
fun HistoryCard(
    isVisible: Boolean = true,
    onDismiss: () -> Unit = {},
    itemList: List<SpendingItem> = listOf()
) {
    val animationDuration = 600
    val overlayStartDelay = animationDuration / 2

    val overlayAlpha by animateFloatAsState(
        targetValue = if (isVisible) 0.8f else 0f,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = overlayStartDelay
        )
    )

    var currentMonth by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    var offsetX by remember { mutableStateOf(0f) }
    var isFlipped by remember { mutableStateOf(false) }
    var rotationY by remember { mutableStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()

    val animatedOffsetX by animateDpAsState(
        targetValue = offsetX.dp,
        animationSpec = tween(
            durationMillis = animationDuration
        )
    )

    // Gesture detection for swipe
    val gestureModifier = Modifier.pointerInput(Unit) {
        detectHorizontalDragGestures(
            onDragEnd = {
                if (offsetX > 0) { // Swipe to the right
                    coroutineScope.launch {
                        offsetX = 0f
                        currentMonth = currentMonth.plusMonths(1)
                    }
                } else if (offsetX < 0) { // Swipe to the left
                    coroutineScope.launch {
                        offsetX = 0f
                        currentMonth = currentMonth.minusMonths(1)
                    }
                }
            }
        ) { change, dragAmount ->
            change.consume()
            offsetX += dragAmount
        }
    }

    // Animation for flipping
    val animatedRotationY by animateFloatAsState(
        targetValue = rotationY,
        animationSpec = tween(durationMillis = 300)
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
                .fillMaxSize()
                .then(gestureModifier),
            contentAlignment = Alignment.BottomCenter
        ) {
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

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp)
                    .offset(x = animatedOffsetX)
                    .graphicsLayer {
                        rotationY = animatedRotationY
                        cameraDistance = 12f * density
                    }
                    .animateContentSize(),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Box(
                    Modifier
                        .background(Color(R.color.background))
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (rotationY >= 90f || rotationY <= -90f) {
                            Text(
                                text = "hello",
                                fontFamily = FontFamily(Font(R.font.open)),
                                fontSize = 30.sp,
                                color = Color.Black
                            )
                        } else {
                            Text(
                                text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                                fontFamily = FontFamily(Font(R.font.open)),
                                fontSize = 30.sp,
                                color = Color.Black,
                                modifier = Modifier.clickable {
                                    coroutineScope.launch {
                                        rotationY = (rotationY + 180) % 360
                                        isFlipped = !isFlipped
                                    }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(30.dp))
                        if (!isFlipped) {
                            val listNow =
                                itemList.filter {
                                    ConvertTime.convertTimestampToDate(it.time)
                                        .contains(currentMonth.monthValue.toString())
                                }
                            Box(
                                modifier = Modifier
                                    .size(150.dp)
                            ) {

                                PieChart(
                                    data = SumAllCategories.sumAllCategoriesList(listNow),
                                    item = listNow
                                )
                            }
                            Spacer(modifier = Modifier.size(30.dp))
                            val lastMonth = SumAllCategories.sumAllCategoriesList(itemList.filter {
                                ConvertTime.convertTimestampToDate(it.time)
                                    .contains(currentMonth.minusMonths(1).monthValue.toString())
                            })
                            val secondMouth = SumAllCategories.sumAllCategoriesList(itemList.filter {
                                ConvertTime.convertTimestampToDate(it.time)
                                    .contains(currentMonth.plusMonths(1).monthValue.toString())
                            })
                            val nowCategory = SumAllCategories.sumAllCategoriesList(listNow)
                            val map = mapOf(
                                currentMonth.minusMonths(1)
                                    .format(DateTimeFormatter.ofPattern("MMMM")) to lastMonth.values.sum(),
                                currentMonth.format(DateTimeFormatter.ofPattern("MMMM")) to nowCategory.values.sum(),
                                currentMonth.plusMonths(1)
                                    .format(DateTimeFormatter.ofPattern("MMMM")) to secondMouth.values.sum()
                            )
                            VertikalColumns(map)
                            CategoriesList(
                                itemList,
                                Color(R.color.black),
                                currentMonth.monthValue.toString()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VertikalColumns(
    map: Map<String, Double> = mapOf(
        "Sample-1" to 15000.0,
        "Sample-2" to 12000.0,
        "Sample-3" to 11000.0,
    )
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Box(modifier = Modifier.background(Color(R.color.black))) {
            Box(modifier = Modifier.background(Color(R.color.black))) {
                BarChart(data = map)
            }
        }
    }
}