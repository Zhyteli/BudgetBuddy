package com.budget.buddy.domain.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MainUserDataMouth(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val currency: String = "",
    val balance: Double = 0.0
)