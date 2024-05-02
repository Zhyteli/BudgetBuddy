package com.budget.buddy.domain.items

import com.budget.buddy.presentation.ui.categories.CategoryIcons

data class SpendingItem(
    val imageResourceId: String = CategoryIcons.OTHER.icons,
    val reason: String = "",
    val sum: Double = 0.0,
    val time: Int = 0
)
