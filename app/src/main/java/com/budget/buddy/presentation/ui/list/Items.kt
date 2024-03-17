package com.budget.buddy.presentation.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.budget.buddy.R
import com.budget.buddy.domain.items.SpendingItem

@Preview
@Composable
fun ItemCard(
    item: SpendingItem = SpendingItem(
        imageResourceId = R.drawable.ic_launcher_foreground,
        reason = "Shopping",
        sum = 100.0
    ),
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = item.imageResourceId),
                contentDescription = "Spending Image",
                modifier = Modifier.size(50.dp)
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = item.reason,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "%.2f".format(item.sum),
                fontSize = 14.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(end = 50.dp)
            )
        }
    }
}
