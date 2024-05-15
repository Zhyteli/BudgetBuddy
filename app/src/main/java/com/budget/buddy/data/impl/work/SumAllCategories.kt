package com.budget.buddy.data.impl.work

import com.budget.buddy.domain.items.SpendingItem
import java.text.NumberFormat
import java.util.Locale

object SumAllCategories {
    fun sumAllCategoriesList(listAll: List<SpendingItem>): Map<String, Double> {
        val numberFormat = NumberFormat.getInstance(Locale.GERMANY)
        numberFormat.maximumFractionDigits = 2
        val map = listAll.groupBy { it.imageResourceId }
            .mapValues { (_, values) ->
                values.filter { it.sum < 0 }
                    .sumOf {
                        val formattedTotalSpent = numberFormat.format(it.sum)
                        numberFormat.parse(formattedTotalSpent)?.toDouble() ?: 0.0
                    }
            }.map {
                val formattedTotalSpent = numberFormat.format(it.value)
                it.key to numberFormat.parse(formattedTotalSpent)!!.toDouble()
            }
            .toMap()
        return map
    }
}