package com.budget.buddy.presentation.ui.additem

import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.budget.buddy.domain.items.SpendingItem

@Preview
@Composable
fun EditItem(
    isVisible: Boolean = true,
    item: MutableState<SpendingItem> = mutableStateOf(SpendingItem()),
    onDismiss: () -> Unit = {},
    addNewItem: (SpendingItem) -> Unit = {},
) {
    val animationDuration = 600 // Duration for the bottom sheet slide animation
    val overlayStartDelay =
        animationDuration / 2 // Start the overlay animation halfway through the bottom sheet animation
    val sumUser = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    Log.d("TEST_FSS", "newSpendingItem: $item")
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
                    .wrapContentHeight(align = Alignment.Bottom)
                    .height(400.dp)
                    .imePadding(),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(100.dp)
                                .background(Color.LightGray)
                                .clickable { focusRequester.requestFocus() }
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            BasicTextField(
                                value = sumUser.value,
                                onValueChange = { sumUser.value = it },
                                textStyle = TextStyle(color = Color.Black, fontSize = 20.sp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester),
                                decorationBox = { innerTextField ->
                                    if (sumUser.value.isEmpty()) {
                                        Text(
                                            if (item.value.description == "") "Element comments" else item.value.description,
                                            color = Color.DarkGray,
                                            fontSize = 20.sp
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                        }
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            val newSpendingItem = SpendingItem(
                                id = item.value.id,
                                imageResourceId = item.value.imageResourceId,
                                description = sumUser.value,
                                reason = item.value.reason,
                                sum = item.value.sum,
                                time = item.value.time,
                                transaction = item.value.transaction
                            )
                            addNewItem(newSpendingItem)
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
