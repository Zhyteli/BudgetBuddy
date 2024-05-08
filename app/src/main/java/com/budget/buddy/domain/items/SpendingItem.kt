package com.budget.buddy.domain.items

import com.budget.buddy.presentation.ui.categories.CategoryIcons

data class SpendingItem(
    val id: Int = 0,
    val imageResourceId: String = CategoryIcons.OTHER.icons,
    val reason: String = "",
    val sum: Double = 0.0,
    val time: Int = 0,
    val description: String = "",
    // прибовление к сумме это true, вычет это false
    val transaction: Boolean = false
)
