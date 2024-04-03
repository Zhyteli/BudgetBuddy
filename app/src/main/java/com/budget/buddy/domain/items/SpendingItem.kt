package com.budget.buddy.domain.items

import com.budget.buddy.R

data class SpendingItem(
    val imageResourceId: Int = R.drawable.ic_launcher_foreground,
    val reason: String = "",
    val sum: Double = 0.0,
    val time: Int = 0
)
