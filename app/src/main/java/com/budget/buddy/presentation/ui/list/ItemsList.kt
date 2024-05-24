package com.budget.buddy.presentation.ui.list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.budget.buddy.R
import com.budget.buddy.domain.items.SpendingItem
import com.budget.buddy.presentation.ui.them.Colors
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun ItemsList(
    items: List<SpendingItem> = listOf(),
    onDelete: (SpendingItem) -> Unit,
    onEdit: (SpendingItem) -> Unit,
) {
    GroupedItemsList(items,
        onDelete = {
            Log.d("ItemsList", "onDelete: $it")
            onDelete(it)
        },
        onEdit = {
            Log.d("ItemsList", "onEdit: $it")
            onEdit(it)
        }
    )
}

@Composable
fun HeaderItem(header: String) {
    Card(
        Modifier.padding(horizontal = 15.dp, vertical = 10.dp), colors = CardDefaults.cardColors(
            containerColor = Colors.Surface // Slightly lighter card background
        )
    ) {
        Text(
            text = header,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.open)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupedItemsList(
    items: List<SpendingItem>,
    onDelete: (SpendingItem) -> Unit,
    onEdit: (SpendingItem) -> Unit,
) {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    // Grouping transactions by date
    val groupedItems = items.groupBy {
        val date = LocalDate.ofEpochDay((it.time / (24 * 60 * 60)).toLong())
        when (date) {
            today -> stringResource(R.string.today)
            yesterday -> stringResource(R.string.yesterday)
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

            itemsIndexed(items = transactions, key = { index, item ->
                // Ensure the key is unique. Use item.id and index as a fallback if id is not unique
                "${item.id}_${index}"
            }) { _, item ->
                val dismissState = rememberDismissState()
                if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                    onDelete(item)
                }

                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                    background = {
                        DelItems(dismissState.dismissDirection ?: DismissDirection.StartToEnd)
                    },
                    dismissContent = {
                        ItemCard(item)
                    }
                )

                // Handling the action and reset after a gesture is detected
                LaunchedEffect(dismissState.currentValue) {
                    if (dismissState.currentValue == DismissValue.DismissedToEnd) {
                        onEdit(item)  // Assuming edit does not need to remove the item
                        dismissState.reset()  // Reset after performing the action
                    }
                }
            }
        }
    }
}

