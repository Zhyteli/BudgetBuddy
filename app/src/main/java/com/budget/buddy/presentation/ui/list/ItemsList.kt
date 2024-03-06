package com.budget.buddy.presentation.ui.list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.budget.buddy.domain.items.SpendingItem
import androidx.compose.foundation.lazy.items

@Composable
fun ItemsList(items: List<SpendingItem> = listOf()) {
    LazyColumn(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        items(items) { item ->
            ItemCard(item)
        }
    }
}