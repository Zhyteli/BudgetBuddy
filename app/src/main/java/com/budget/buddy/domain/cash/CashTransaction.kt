package com.budget.buddy.domain.cash

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CashTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int = (0..10000).random(),
    val amount: Double,
    val date: Int,
    val description: String? = null,
    val descriptionFull: String? = null,
    val type: Int,
    val transaction: Boolean = false
)