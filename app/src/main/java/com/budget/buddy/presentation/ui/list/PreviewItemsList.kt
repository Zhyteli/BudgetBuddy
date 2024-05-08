package com.budget.buddy.presentation.ui.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.budget.buddy.R
import com.budget.buddy.domain.items.SpendingItem

@Preview(showBackground = true)
@Composable
fun PreviewItemsList(
    sItem: List<SpendingItem> = listOf(),
) {
    if (sItem.isEmpty()){
        Text(text = "No items")
    }else{
        ItemsList(items = sItem, onDelete = {}, onEdit = {})
    }
}
