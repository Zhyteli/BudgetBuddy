@file:OptIn(ExperimentalMaterial3Api::class)

package com.budget.buddy.presentation.ui.additem

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.budget.buddy.R
import com.budget.buddy.domain.items.SpendingItem
import com.budget.buddy.presentation.ui.categories.CategoriesItem
import com.budget.buddy.presentation.ui.categories.CategoriesItemData
import com.budget.buddy.presentation.ui.categories.CategoryIcons
import com.budget.buddy.presentation.ui.categories.listScrol

@Preview
@Composable
fun BottomSheetComponent(
    isVisible: Boolean = true,
    onDismiss: () -> Unit = {},
    addNewItem: (SpendingItem) -> Unit = {},
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
                    .fillMaxWidth()
                    .height(500.dp),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Box(
                    Modifier
                        .background(Color(R.color.background))
                        .height(500.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            var selected by remember { mutableStateOf(false) }
                            FilterChip(
                                modifier = Modifier.size(width = 100.dp, height = 50.dp),
                                onClick = { selected = !selected },
                                label = {
                                    if (selected) {
                                        Text(
                                            "Income", color = Color.Black,
                                            fontFamily = FontFamily(
                                                Font(R.font.open)
                                            )
                                        )
                                    } else {
                                        Text(
                                            "Costs", color = Color.White,
                                            fontFamily = FontFamily(
                                                Font(R.font.open)
                                            )
                                        )
                                    }
                                },
                                colors = FilterChipDefaults.filterChipColors(Color(R.color.on_primary)),
                                selected = selected,
                                leadingIcon = if (selected) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Add,
                                            contentDescription = "Done icon",
                                            tint = Color.Black,
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                        transaction = true
                                    }
                                } else {
                                    transaction = false
                                    null
                                },
                            )
                            Box(modifier = Modifier.padding(top = 3.dp, start = 10.dp)) {
                                BasicTextField(
                                    value = sumUser.value,
                                    onValueChange = { sumUser.value = it },
                                    decorationBox = { innerTextField ->
                                        if (sumUser.value.isEmpty()) {
                                            Text(
                                                "0",
                                                color = Color.DarkGray,
                                                fontSize = 30.sp,
                                                fontFamily = FontFamily(
                                                    Font(R.font.open)
                                                )
                                            )
                                        }
                                        innerTextField()
                                    },
                                    singleLine = true,
                                    textStyle = TextStyle(color = Color.Black, fontSize = 30.sp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }
                        }
                        LazyRow(
                            modifier = Modifier.padding(vertical = 8.dp),
                        ) {
                            items(listScrol) { item ->
                                Spacer(modifier = Modifier.size(8.dp))
                                CategoriesItem(item) {
                                    categoryIcons = it
                                }
                            }
                        }
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                addNewItem(
                                    SpendingItem(
                                        imageResourceId = categoryIcons.icon,
                                        reason = categoryIcons.title,
                                        sum = if (transaction) sumUser.value.toDouble() else -1 * sumUser.value.toDouble(),
                                        time = (System.currentTimeMillis() / 1000).toInt(),
                                        transaction = transaction
                                    )
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }
}