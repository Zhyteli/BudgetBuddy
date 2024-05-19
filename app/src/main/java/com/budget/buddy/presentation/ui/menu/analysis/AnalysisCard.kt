package com.budget.buddy.presentation.ui.menu.analysis

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.budget.buddy.R

@Preview
@Composable
fun AnalysisCard(
    isVisible: Boolean = true,
    onDismiss: () -> Unit = {},
    analysis: String? = null
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
                    .animateContentSize(),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Box(
                    Modifier
                        .background(Color(R.color.background))
                        .fillMaxSize()
                ) {
                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
//                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.analysis))
//                        val progress by animateLottieCompositionAsState(composition)
//                        LottieAnimation(
//                            composition = composition,
//                            progress = { progress },
//                        )
                        MaterialTheme {
                            AnalysisScreen(analysis = analysis) // Pass actual analysis data when available
                        }
                        Button(
                            onClick = {
                                onDismiss()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text(
                                text = "Back",
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.open))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingAnimation() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color.Gray,
                strokeWidth = 4.dp,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Analyzing data...",
                fontFamily = FontFamily(Font(R.font.open)),
                fontSize = 20.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun AnalysisScreen(analysis: String?) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        if (analysis == null) {
            LoadingAnimation()
        } else {
            Text(
                text = analysis,
                fontFamily = FontFamily(Font(R.font.open)),
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}
