package com.budget.buddy.domain.items

data class SpendingItem(
    val imageResourceId: Int,
    val reason: String,
    val sum: Double
)
