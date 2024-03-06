package com.budget.buddy.presentation.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.budget.buddy.R
import com.budget.buddy.domain.items.SpendingItem

@Preview(showBackground = true)
@Composable
fun PreviewItemsList(
    sItem: List<SpendingItem> = listOf(),
) {
    ItemsList(items = sItem)
}
