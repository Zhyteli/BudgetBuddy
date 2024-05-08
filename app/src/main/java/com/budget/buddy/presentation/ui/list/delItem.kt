package com.budget.buddy.presentation.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DelItems(dismissDirection: DismissDirection = DismissDirection.EndToStart) {
    val icon = when (dismissDirection) {
        DismissDirection.StartToEnd -> Icons.Filled.Edit
        DismissDirection.EndToStart -> Icons.Filled.Delete
    }
    val contentDescription = when (dismissDirection) {
        DismissDirection.StartToEnd -> "Edit"
        DismissDirection.EndToStart -> "Delete"
    }
    val backgroundColor = when (dismissDirection) {
        DismissDirection.StartToEnd -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        DismissDirection.EndToStart -> MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 15.dp, vertical = 3.dp),
    ) {
        val arrangement = if (dismissDirection == DismissDirection.StartToEnd) {
            Arrangement.Start
        } else {
            Arrangement.End
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            horizontalArrangement = arrangement
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(24.dp),
                tint = when (dismissDirection) {
                    DismissDirection.StartToEnd -> MaterialTheme.colorScheme.primary
                    DismissDirection.EndToStart -> MaterialTheme.colorScheme.error
                }
            )
        }

    }
}