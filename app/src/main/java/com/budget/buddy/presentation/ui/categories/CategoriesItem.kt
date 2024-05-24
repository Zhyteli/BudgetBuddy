package com.budget.buddy.presentation.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.budget.buddy.R
import com.budget.buddy.presentation.ui.them.Colors

@Preview
@Composable
fun CategoriesItem(
    item: CategoriesItemData = CategoriesItemData(
        CategoryIcons.OTHER.title,
        CategoryIcons.OTHER.icons
    ),
    onClick: (CategoriesItemData) -> Unit = {},
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = Modifier.clickable(onClick = {
            onClick(item)
        }),
        colors = CardDefaults.cardColors(
            containerColor = Colors.Surface // Slightly lighter card background
        )
    ) {
        Box(
            Modifier
                .wrapContentSize()) {
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(5.dp)
            ) {
                Text(
                    text = item.icon,
                    modifier = Modifier
                        .padding(end = 5.dp),
                    fontSize = 24.sp
                )
                Text(
                    item.title,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = Color.White,
                    fontFamily = FontFamily(
                        Font(R.font.open)
                    )
                )
            }
        }
    }
}