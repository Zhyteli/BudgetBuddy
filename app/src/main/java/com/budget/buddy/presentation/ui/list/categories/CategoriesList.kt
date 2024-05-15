package com.budget.buddy.presentation.ui.list.categories

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.budget.buddy.R
import com.budget.buddy.domain.items.SpendingItem
import com.budget.buddy.domain.mapper.MapperColorsToItem
import com.budget.buddy.domain.mapper.convert.ConvertTime
import com.budget.buddy.presentation.ui.list.ItemCard
import java.time.LocalDate

@Preview
@Composable
fun CategoriesList(
    itemds: List<SpendingItem> = listOf(SpendingItem()),
    color: Color = Color(R.color.black),
    mouth: String = LocalDate.now().monthValue.toString()
) {
    LazyColumn(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        items(itemds) { item ->
            if(ConvertTime.convertTimestampToDate(item.time) == mouth){
                ItemCard(item, MapperColorsToItem.mapToColor(item))
            }
        }
    }
}