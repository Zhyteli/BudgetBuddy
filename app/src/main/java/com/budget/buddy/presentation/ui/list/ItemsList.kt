package com.budget.buddy.presentation.ui.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.budget.buddy.domain.items.SpendingItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun ItemsList(items: List<SpendingItem> = listOf()) {
    GroupedItemsList(items)
}

@Composable
fun HeaderItem(header: String) {
    BasicText(
        text = header,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}
@Composable
fun GroupedItemsList(items: List<SpendingItem>) {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    // Grouping transactions by date
    val groupedItems = items.groupBy {
        val date = LocalDate.ofEpochDay((it.time / (24 * 60 * 60)).toLong()) // Assuming `time` is in seconds
        when {
            date == today -> "Today"
            date == yesterday -> "Yesterday"
            else -> date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        }
    }

    LazyColumn(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        groupedItems.forEach { (header, transactions) ->
            item {
                // Display the header
                HeaderItem(header)
            }

            items(transactions) { item ->
                // Display each SpendingItem in the group
                ItemCard(item)
            }
        }
    }
}
